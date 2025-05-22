package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.LeaderboardUser

/**
 * Interface defining gamification-related data operations.
 */
interface GamificationRepository {
    /**
     * Fetches the global ranking of users.
     * @param category Optional category filter for the ranking (null for overall ranking).
     * @return List of [LeaderboardUser] ordered by ranking position.
     * @throws Exception if there's an error fetching the ranking data.
     */
    suspend fun getGlobalRanking(category: String? = null): List<LeaderboardUser>
}
