package com.example.habitflow_app.features.authentication.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects for user login.
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)