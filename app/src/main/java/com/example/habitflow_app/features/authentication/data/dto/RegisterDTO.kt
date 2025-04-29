package com.example.habitflow_app.features.authentication.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects for user registration.
 */
@Serializable
data class RegisterUserRequest(
    val email: String,
    val password: String,
    val role: String
)

@Serializable
data class CreateProfileRequest(
    val id: String,
    val full_name: String,
    val username: String,
    val streak: Int,
    val best_streak: Int,
    val avatar_url: String
)

