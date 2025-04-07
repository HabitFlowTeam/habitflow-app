package com.example.habitflow_app.features.authentication.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for user registration.
 */
@Serializable
data class RegisterDto(
    val fullName: String,
    val username: String,
    val email: String,
    val password: String
)
