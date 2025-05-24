package com.example.habitflow_app.domain.models

/**
 * Domain model to represent a user in the global ranking.
 */
data class LeaderboardUser(
    val id: String,
    val fullName: String,
    val streak: Int,
    val avatarUrl: String,
    val rank: Int = 0
)
