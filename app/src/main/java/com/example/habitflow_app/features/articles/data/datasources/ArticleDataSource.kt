package com.example.habitflow_app.features.articles.data.datasources

import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.features.articles.data.dto.ProfileArticlesDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
){
    companion object {
        private const val TAG = "ArticleDataSource"
    }

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
                    imageUrl = dto.imageUrl,
                    likes = dto.likes,
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener artículos", e)
            throw Exception("Error al obtener artículos: ${e.message}")
        }
    }
}
