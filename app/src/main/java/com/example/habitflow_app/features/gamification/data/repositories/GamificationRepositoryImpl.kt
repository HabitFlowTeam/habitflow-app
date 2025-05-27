package com.example.habitflow_app.features.gamification.data.repositories

import com.example.habitflow_app.domain.models.LeaderboardUser
import com.example.habitflow_app.domain.repositories.GamificationRepository
import com.example.habitflow_app.features.gamification.data.datasources.GamificationDataSource
import javax.inject.Inject

/**
 * Implementation of GamificationRepository that mediates between domain layer and data sources.
 * Handles business logic for gamification-related operations.
 *
 * @property gamificationDataSource The gamification data source
 */
class GamificationRepositoryImpl @Inject constructor(
    private val gamificationDataSource: GamificationDataSource
) : GamificationRepository {

    /**
     * Retrieves the global ranking of users, optionally filtered by a specific category.
     *
     * @param category Optional category filter for the ranking. If null, returns overall ranking.
     * @return List of LeaderboardUser objects representing the ranking
     */
    override suspend fun getGlobalRanking(category: String?): List<LeaderboardUser> {
        return gamificationDataSource.getGlobalRanking(category)
    }
}
