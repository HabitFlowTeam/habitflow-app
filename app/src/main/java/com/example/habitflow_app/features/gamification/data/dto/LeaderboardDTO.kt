package com.example.habitflow_app.features.gamification.data.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects for gamification responses.
 */

/**
 * Represents the leaderboard response containing a list of users with their ranking information.
 * @property data List of [LeaderboardUserDTO] containing user ranking details.
 */
@Serializable
data class LeaderboardResponse(
    val data: List<LeaderboardUserDTO>
)

/**
 * Represents a user in the leaderboard with their ranking information.
 * @property id Unique identifier of the user.
 * @property full_name Full name of the user.
 * @property streak Current streak count of the user.
 * @property avatar_url URL of the user's avatar image.
 */
@Serializable
data class LeaderboardUserDTO(
    val id: String,
    val full_name: String,
    val streak: Int,
    val avatar_url: String
)

/**
 * Represents the habit ranking response containing a list of users with their habit streaks.
 * @property data List of [HabitRankingDTO] containing habit streak information.
 */
@Serializable
data class HabitRankingResponse(
    val data: List<HabitRankingDTO>
)

/**
 * Represents a user's habit streak information.
 * @property user_id Unique identifier of the user.
 * @property streak Current streak count for the habit.
 */
@Serializable
data class HabitRankingDTO(
    val user_id: String,
    val streak: Int
)

/**
 * Represents the profile ranking response containing basic user profile information.
 * @property data List of [ProfileRankingDTO] containing user profile details.
 */
@Serializable
data class ProfileRankingResponse(
    val data: List<ProfileRankingDTO>
)

/**
 * Represents basic user profile information for ranking purposes.
 * @property id Unique identifier of the user.
 * @property full_name Full name of the user.
 * @property avatar_url URL of the user's avatar image.
 */
@Serializable
data class ProfileRankingDTO(
    val id: String,
    val full_name: String,
    val avatar_url: String
)
