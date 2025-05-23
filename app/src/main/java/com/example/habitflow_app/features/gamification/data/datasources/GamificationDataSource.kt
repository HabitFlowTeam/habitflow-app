package com.example.habitflow_app.features.gamification.data.datasources

import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.LeaderboardUser
import javax.inject.Inject

/**
 * Data source implementation for gamification operations using Directus API.
 * Handles direct communication with Directus services for leaderboard and ranking operations.
 *
 * @property directusApiService The Directus API service for making network requests
 */
class GamificationDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
) {
    private companion object {
        const val TAG = "GamificationDataSource"
    }

    /**
     * Retrieves the global ranking of users or filtered by a specific category.
     *
     * @param categoryId Optional category ID to filter the ranking. If null, returns global ranking.
     * @return List of [LeaderboardUser] objects representing the ranking
     * @throws Exception if there's an error fetching the ranking data
     */
    suspend fun getGlobalRanking(categoryId: String? = null): List<LeaderboardUser> {
        try {
            Log.d(
                TAG,
                "Obteniendo ranking ${if (categoryId != null) "para categoría: $categoryId" else "general"}"
            )

            if (categoryId == null) {
                // Ranking global basado en streak de perfiles
                return getGlobalProfileRanking()
            } else {
                // Ranking por categoría basado en streak de hábitos
                return getCategoryHabitRanking(categoryId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener ranking global", e)
            throw Exception("Error al obtener ranking global: ${e.message}")
        }
    }

    /**
     * Fetches the global ranking based on user profiles' streaks.
     *
     * @return List of [LeaderboardUser] ordered by their streak
     * @throws Exception if the API request fails or returns unsuccessful response
     */
    private suspend fun getGlobalProfileRanking(): List<LeaderboardUser> {
        val response = directusApiService.getGlobalRanking()
        if (!response.isSuccessful) {
            Log.e(TAG, "Error al obtener ranking global: ${response.code()} ${response.message()}")
            throw Exception("Error al obtener ranking global: ${response.code()}")
        }

        val profileData = response.body()?.data ?: emptyList()
        Log.d(TAG, "Ranking global obtenido exitosamente: ${profileData.size} usuarios")

        return profileData.mapIndexed { index, profileDto ->
            LeaderboardUser(
                id = profileDto.id,
                fullName = profileDto.full_name,
                streak = profileDto.streak,
                avatarUrl = profileDto.avatar_url,
                rank = index + 1
            )
        }
    }

    /**
     * Fetches the ranking for a specific category based on habits' streaks.
     * This method performs two API calls:
     * 1. Gets habits for the specified category with their streaks and user IDs
     * 2. Gets profile data for those user IDs
     *
     * @param categoryName The name of the category to filter by
     * @return List of [LeaderboardUser] ordered by their streak in the specified category
     * @throws Exception if any API request fails or returns unsuccessful response
     */
    private suspend fun getCategoryHabitRanking(categoryName: String): List<LeaderboardUser> {
        // Paso 1: Obtener hábitos por categoría
        val habitResponse = directusApiService.getCategoryRanking(categoryName)
        if (!habitResponse.isSuccessful) {
            Log.e(
                TAG,
                "Error al obtener ranking por categoría: ${habitResponse.code()} ${habitResponse.message()}"
            )
            throw Exception("Error al obtener ranking por categoría: ${habitResponse.code()}")
        }

        val habitData = habitResponse.body()?.data ?: emptyList()
        if (habitData.isEmpty()) {
            Log.d(TAG, "No se encontraron hábitos para la categoría: $categoryName")
            return emptyList()
        }

        // Paso 2: Obtener perfiles para los user_ids encontrados
        val userIds = habitData.map { it.user_id }.joinToString(",")
        val profileResponse = directusApiService.getProfileRanking(userIds)

        if (!profileResponse.isSuccessful) {
            Log.e(
                TAG,
                "Error al obtener perfiles para ranking: ${profileResponse.code()} ${profileResponse.message()}"
            )
            throw Exception("Error al obtener perfiles para ranking: ${profileResponse.code()}")
        }

        val profileData = profileResponse.body()?.data ?: emptyList()

        // Paso 3: Combinar datos de hábitos y perfiles
        val profileMap = profileData.associateBy { it.id }

        return habitData.mapIndexed { index, habitDto ->
            val profile = profileMap[habitDto.user_id]
            LeaderboardUser(
                id = habitDto.user_id,
                fullName = profile?.full_name ?: "Usuario",
                streak = habitDto.streak,
                avatarUrl = profile?.avatar_url ?: "",
                rank = index + 1
            )
        }
    }
}
