package com.example.habitflow_app.features.habits.data.datasources

import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
import org.json.JSONObject
import java.time.LocalDate
import javax.inject.Inject

class HabitsDataSource @Inject constructor(
    private val directusApiService: DirectusApiService,
    private val authRepository: AuthRepository,
    private val tokenExtractor: ExtractInfoToken
) {
    suspend fun getHabits(userId: String): List<Habit> {
        val response = directusApiService.getHabits(userId)
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Response body is null")
        }
        else {
            throw Exception("Falló algo en algun lado jaja")
        }
    }

    suspend fun createHabit(request: CreateHabitRequest): HabitResponse {
        // 1. Autenticación y obtención de userId
        val userId = getAuthenticatedUserId()

        Log.d("FLOW", "Step 1 - Start")
        val habitId = createHabitInApi(request, userId)
        Log.d("FLOW", "Step 2 - Habit created: $habitId")

        // 3. Crear días asociados
        createHabitDaysInApi(habitId, request.selectedDays)
        Log.d("FLOW", "Step 3 - Days created")

        // 4. Crear tracking inicial
        if (request.initialTracking) {
            createInitialTrackingInApi(habitId)
            Log.d("FLOW", "Step 4 - Tracking created")
        }

        // Devolvemos los datos del hábito creado
        return HabitResponse(
            name = request.name,
            categoryId = request.categoryId,
            selectedDays = request.selectedDays,
            reminderTime = request.reminderTime
        )
    }

    private suspend fun getAuthenticatedUserId(): String {
        val token = authRepository.getAccessTokenOnce() ?: throw Exception()
        return tokenExtractor.extractUserIdFromToken(token)
    }

    private suspend fun createHabitInApi(request: CreateHabitRequest, userId: String): String {
        val response = directusApiService.createHabit(
            HabitApiRequest(
                name = request.name,
                userId = userId,
                categoryId = request.categoryId,
                reminderTime = request.reminderTime?.toString(),
                notificationsEnabled = request.notificationsEnabled
            )
        )

        if (!response.isSuccessful) {
            throw Exception("Error creating habit: ${response.errorBody()?.string()}")
        }

        val responseBody = response.body()?.string()
        return try {
            JSONObject(responseBody ?: "").getJSONObject("data").getString("id").also {
                Log.d("API", "Extracted habit ID: $it")
            }
        } catch (e: Exception) {
            Log.e("API", "Failed to parse response: $responseBody", e)
            throw Exception("Failed to parse habit ID from response")
        }
    }

    private suspend fun createHabitDaysInApi(habitId: String, days: List<String>) {
        val results = days.map { dayId ->
            val response = directusApiService.createHabitDay(HabitDayApiRequest(habitId, dayId))

            if (!response.isSuccessful) {
                Log.d(">>>", "error in createHabitDaysInApi ")
            }

            response.isSuccessful
        }

        if (results.any { !it }) {
            throw Exception("Failed to create some habit days")
        }
    }

    private suspend fun createInitialTrackingInApi(habitId: String) {
        val response = directusApiService.createHabitTracking(
            HabitTrackingApiRequest(
                isChecked = false,
                trackingDate = LocalDate.now().toString(),
                habitId = habitId
            )
        )
        if (!response.isSuccessful) {
            throw Exception("Failed to create initial tracking")
        }
    }

    suspend fun getHabitDays(habitId: String): List<HabitDayResponse> {
        val response = directusApiService.getHabitDays(habitId)
        if (!response.isSuccessful) {
            throw Exception("Error getting habit days: ${response.errorBody()?.string()}")
        }
        return response.body() ?: emptyList()
    }

    suspend fun updateHabitDays(request: UpdateHabitDaysRequest): HabitUpdateResponse {
        // 1. Eliminar días existentes
        directusApiService.deleteHabitDay(request.habitId)

        // 2. Crear nuevos días
        val createdCount = request.days.map { dayId ->
            directusApiService.createHabitDay(
                HabitDayApiRequest(request.habitId, dayId)
            ).isSuccessful
        }.count { it }

        return HabitUpdateResponse(
            success = createdCount == request.days.size,
            updatedCount = createdCount
        )
    }

    suspend fun softDeleteHabit(habitId: String): Boolean {
        val response = directusApiService.softDeleteHabit(habitId)
        if (response.isSuccessful) {
            return response.body()?.isDeleted == true
        } else {
            throw Exception("Failed to delete habit: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getCategories(): List<Category> {
        val response = directusApiService.getCategories()
        if (response.isSuccessful) {
            return response.body()?.data?.map {
                Category(
                    id = it.id,
                    name = it.name,
                    description = it.description
                )
            } ?: emptyList()
        } else {
            throw Exception("Failed to fetch categories: ${response.errorBody()?.string()}")
        }
    }

}