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
    val fullName: String,
    val username: String,
    val streak: Int,
    val bestStreak: Int,
    val avatarUrl: String
)

