package com.example.habitflow_app.features.category.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects for category related requests and responses.
 */

/**
 * Response containing a list of categories.
 * @property data List of [CategoryDTO] containing category details.
 */
@Serializable
data class CategoriesResponse(
    val data: List<CategoryDTO>
)

/**
 * Represents a single category with its details.
 * @property id Unique identifier of the category.
 * @property name Name of the category.
 * @property description Description of the category.
 */
@Serializable
data class CategoryDTO(
    val id: String,
    val name: String,
    val description: String
)
