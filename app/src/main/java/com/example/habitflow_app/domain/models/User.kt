package com.example.habitflow_app.domain.models

import com.example.habitflow_app.BuildConfig

/**
 * Domain model for the user.
 */
data class User(
    val id: String,
    val email: String,
    val password: String,
    val role: String = BuildConfig.ROLE_ID
)
