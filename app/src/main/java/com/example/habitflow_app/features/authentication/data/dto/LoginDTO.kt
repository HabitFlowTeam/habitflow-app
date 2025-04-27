package com.example.habitflow_app.features.authentication.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for user login.
 */
@Serializable
data class LoginDto(
    val email: String,
    val password: String
)