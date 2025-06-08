package com.example.habitflow_app.features.articles.data.datasources

import android.util.Log
import com.example.habitflow_app.core.exceptions.ApiException
import com.example.habitflow_app.core.exceptions.NetworkConnectionException
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.models.RankedArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Data source for fetching article data from the Directus API service.
 * Handles network and API exceptions.
 */
class ArticleDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
) {
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
    suspend fun getUserArticles(userId: String): List<ProfileArticle> =
        withContext(Dispatchers.IO) {
            try {
                val response = directusApiService.getUserArticles(userId)
                if (!response.isSuccessful) {
                    Log.e(
                        TAG,
                        "Error al obtener artículos: ${response.code()} ${response.message()}"
                    )
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

    suspend fun getRankedArticles(): List<RankedArticle> = withContext(Dispatchers.IO) {
        try {
            val response = directusApiService.getRankedArticles()
            if (!response.isSuccessful) {
                Log.e(TAG, "Error al obtener artículos: ${response.code()} ${response.message()}")
                throw Exception("Error al obtener artículos: ${response.code()}")
            }
            val articles = response.body()?.data ?: emptyList()
            articles.map { dto ->
                RankedArticle(
                    title = dto.title,
                    content = dto.content,
                    authorName = dto.author_name,
                    authorImageUrl = dto.author_image_url,
                    likesCount = dto.likes_count,
                    articleId = dto.id,
                    categoryName = dto.category_name,
                    createdAt = dto.created_at,
                    imageUrl = dto.image_url
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener artículos", e)
            throw Exception("Error al obtener artículos: ${e.message}")
        }
    }
}
