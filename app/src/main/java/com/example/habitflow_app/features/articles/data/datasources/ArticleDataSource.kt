package com.example.habitflow_app.features.articles.data.datasources

import android.util.Log
import com.example.habitflow_app.core.exceptions.ApiException
import com.example.habitflow_app.core.exceptions.NetworkConnectionException
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.ProfileArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Data source for fetching article data from the Directus API service.
 * Handles network and API exceptions.
 */
class ArticleDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
){
    companion object {
        private const val TAG = "ArticleDataSource"
    }

    /**
     * Retrieves the list of articles from the Directus service.
     *
     * @return List of [ProfileArticle] objects fetched from the backend.
     * @throws ApiException if an API error or unexpected error occurs.
     * @throws NetworkConnectionException if a network connection error occurs.
     */
    suspend fun getUserArticles(userId: String): List<ProfileArticle> = withContext(Dispatchers.IO) {
        try {
            val response = directusApiService.getUserArticles(userId)
            if (!response.isSuccessful) {
                Log.e(TAG, "Error al obtener artículos: ${response.code()} ${response.message()}")
                throw Exception("Error al obtener artículos: ${response.code()}")
            }
            val articles = response.body()?.data ?: emptyList()
            articles.map { dto ->
                ProfileArticle(
                    title = dto.title,
                    imageUrl = dto.image_url,
                    likes = dto.likes_count,
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener artículos", e)
            throw Exception("Error al obtener artículos: ${e.message}")
        }
    }
}


