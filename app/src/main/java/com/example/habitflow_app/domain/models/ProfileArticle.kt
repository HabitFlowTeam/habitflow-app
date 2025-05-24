package com.example.habitflow_app.domain.models

/**
 * Domain model to represent articles from a user.
 *
 * @property title The title of the article
 * @property likes The number of likes the article has received
 * @property imageUrl The URL of the article's image (nullable)
 */
data class ProfileArticle(
    val title: String,
    val likes: Int,
    val imageUrl: String? = null,
)
