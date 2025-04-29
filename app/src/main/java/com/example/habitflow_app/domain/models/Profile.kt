package com.example.habitflow_app.domain.models

import com.example.habitflow_app.BuildConfig

/**
 * Domain model for the profile.
 */
data class Profile(
    val id: String,
    val fullName: String,
    val username: String,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val avatarUrl: String = BuildConfig.DEFAULT_PROFILE_URL
)
