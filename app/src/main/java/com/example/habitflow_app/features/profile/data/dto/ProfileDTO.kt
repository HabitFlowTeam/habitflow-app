package com.example.habitflow_app.features.profile.data.dto

import com.example.habitflow_app.domain.models.Profile

@Serializable
data class ProfileDTO(
    val id: String,
    val full_name: String,
    val username: String,
    val streak: Int,
    val best_streak: Int,
    val avatar_url: String?,
    val created_at: String? = null,
    val updated_at: String? = null
) {
    fun toDomainModel(): Profile {
        return Profile(
            id = id,
            fullName = full_name,
            username = username,
            streak = streak,
            bestStreak = best_streak,
            avatarUrl = avatar_url ?: ""
        )
    }
}