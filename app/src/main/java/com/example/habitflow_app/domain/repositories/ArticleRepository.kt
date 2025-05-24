package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.ProfileArticle

interface ArticleRepository {
    suspend fun getUserArticles(userId: String): List<ProfileArticle>
}