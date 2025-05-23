package com.example.habitflow_app.features.articles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileArticlesResponse(
    val data: List<ProfileArticlesDTO>
)

@Serializable
data class ProfileArticlesDTO(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val likes: Int,
    val userId: String
)
