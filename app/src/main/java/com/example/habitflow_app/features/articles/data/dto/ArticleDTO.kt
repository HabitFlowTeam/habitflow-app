package com.example.habitflow_app.features.articles.data.dto

import kotlinx.serialization.Serializable

/**
 * DTO for the response containing a list of user articles.
 *
 * @property data List of ProfileArticlesDTO representing the user's articles
 */
@Serializable
data class ProfileArticlesResponse(
    val data: List<ProfileArticlesDTO>
)

/**
 * DTO representing an article for a user profile.
 *
 * @property title The title of the article
 * @property image_url The URL of the article's image (nullable)
 * @property likes_count The number of likes the article has received
 */
@Serializable
data class ProfileArticlesDTO(
    val title: String,
    val image_url: String?,
    val likes_count: Int
)
