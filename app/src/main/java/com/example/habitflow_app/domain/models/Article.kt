package com.example.habitflow_app.domain.models

/**
 * Domain model for the article.
 */
data class Article(
    val id: String,
    val title: String,
    val content: String,
    val imageUrl: String? = null,
    val isDeleted: Boolean = false,
    val userId: String,
    val categoryId: String
)

/**
 * Junction model representing a user liking an article.
 */
data class ArticleLiked(
    val userId: String,
    val articleId: String
)

/**
 * Junction model representing a user saving an article for later.
 */
data class ArticleSaved(
    val userId: String,
    val articleId: String
)