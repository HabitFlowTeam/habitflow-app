package com.example.habitflow_app.features.articles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateArticleRequest(
    val title: String,
    val content: String,
    val image_url: String? = null,
    val category_id: String,
    val user_id: String,
    val is_deleted: Boolean = false
)

@Serializable
data class CreateArticleResponse(
    val data: CreateArticleResponseData
)

@Serializable
data class CreateArticleResponseData(
    val id: String,
    val title: String,
    val content: String,
    val image_url: String?,
    val category_id: String,
    val user_id: String,
    val created_at: String,
    val is_deleted: Boolean
)