package com.example.habitflow_app.domain.models

import java.time.Instant

/**
 * Domain model for the user.
 */
data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String,
    val streak: Int = 0,
    val avatarUrl: String? = null,
    val createdAt: Instant = Instant.now()
)
