package com.example.habitflow_app.domain.models

data class RankedArticle(
    val title: String,
    val content: String,
    val authorName: String,
    val authorImageUrl: String?,
    val likesCount: Int,
    val articleId: String,
    val categoryName: String,
    val createdAt: String,
    val imageUrl: String? = null
)
