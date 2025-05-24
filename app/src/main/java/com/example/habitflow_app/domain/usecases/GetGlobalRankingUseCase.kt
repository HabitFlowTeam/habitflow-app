package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.LeaderboardUser
import com.example.habitflow_app.domain.repositories.GamificationRepository
import javax.inject.Inject

/**
 * Encapsulates the business logic for retrieving global user rankings.
 *
 * @property gamificationRepository The gamification data source
 */
class GetGlobalRankingUseCase @Inject constructor(
    private val gamificationRepository: GamificationRepository
) {
    /**
     * Executes the use case to retrieve the global user ranking.
     * @param category Optional category filter (null for overall ranking)
     * @return List of [LeaderboardUser] ordered by ranking position
     */
    suspend operator fun invoke(category: String? = null): List<LeaderboardUser> {
        return gamificationRepository.getGlobalRanking(category)
    }
}
