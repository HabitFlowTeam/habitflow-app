package com.example.habitflow_app.features.articles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileArticlesResponse(
    val data: List<ProfileArticlesDTO>
)

@Serializable
data class ProfileArticlesDTO(
    val title: String,
    val image_url: String?,
    val likes_count: Int
)
