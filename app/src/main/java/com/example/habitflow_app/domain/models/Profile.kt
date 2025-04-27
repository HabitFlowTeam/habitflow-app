package com.example.habitflow_app.domain.models

/**
 * Domain model for the profile.
 */
data class Profile(
    val id: String,
    val fullName: String,
    val username: String,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val avatarUrl: String? = null,
)
