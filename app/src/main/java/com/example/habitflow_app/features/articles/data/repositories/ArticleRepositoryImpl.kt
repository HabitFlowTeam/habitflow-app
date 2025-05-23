package com.example.habitflow_app.features.articles.data.repositories

import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import com.example.habitflow_app.features.articles.data.datasources.ArticleDataSource
import com.example.habitflow_app.features.articles.data.dto.ProfileArticlesDTO
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleDataSource: ArticleDataSource
): ArticleRepository {
    override suspend fun getUserArticles(userId: String): List<ProfileArticle> {
        return articleDataSource.getUserArticles(userId)
    }
}

