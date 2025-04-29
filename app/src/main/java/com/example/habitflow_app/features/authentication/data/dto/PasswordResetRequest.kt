package com.example.habitflow_app.features.authentication.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String
)