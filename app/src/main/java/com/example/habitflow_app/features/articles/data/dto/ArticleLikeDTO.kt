package com.example.habitflow_app.features.articles.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArticleLikeRequest(
    val user_id: String,
    val article_id: String
)

@Serializable
data class DeleteArticleLikeFilter(
    val filter: DeleteArticleLikeCondition
)

@Serializable
data class DeleteArticleLikeCondition(
    val _and: List<Map<String, Map<String, String>>>
)

fun createDeleteFilter(userId: String, articleId: String): DeleteArticleLikeFilter {
    return DeleteArticleLikeFilter(
        filter = DeleteArticleLikeCondition(
            _and = listOf(
                mapOf("article_id" to mapOf("_eq" to articleId)),
                mapOf("user_id" to mapOf("_eq" to userId))
            )
        )
    )
} 