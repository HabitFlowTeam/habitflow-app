package com.example.habitflow_app.domain.models

/**
 * Domain model to represent a user in the global ranking.
 */
data class ProfileArticle(
    val title: String,
    val likes: Int,
    val imageUrl: String? = null,
)
