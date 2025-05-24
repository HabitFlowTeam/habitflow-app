package com.example.habitflow_app.domain.models

/**
 * Domain model representing a general article.
 *
 * @property id Unique identifier for the article
 * @property title Title of the article
 * @property content Main content of the article
 * @property imageUrl Optional URL for the article's image
 * @property isDeleted Indicates if the article is deleted
 * @property userId ID of the user who created the article
 * @property categoryId ID of the category the article belongs to
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
 * Model representing a user liking an article.
 *
 * @property userId ID of the user who liked the article
 * @property articleId ID of the liked article
 */
data class ArticleLiked(
    val userId: String,
    val articleId: String
)

/**
 * Model representing a user saving an article for later.
 *
 * @property userId ID of the user who saved the article
 * @property articleId ID of the saved article
 */
data class ArticleSaved(
    val userId: String,
    val articleId: String
)

