package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.models.RankedArticle

/**
 * Interface defining the operations for article data access.
 */
interface ArticleRepository {
    /**
     * Retrieves the list of articles for a specific user.
     * @param userId The ID of the user whose articles are to be fetched.
     * @return List of [ProfileArticle] objects.
     * @throws Exception if there's an error fetching the articles.
     */
    suspend fun getUserArticles(userId: String): List<ProfileArticle>

    suspend fun getRankedArticles(): List<RankedArticle>
}

