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

    /**
     * Adds a like to an article for the current user
     * @param articleId The ID of the article to like
     * @return true if the operation was successful, false otherwise
     */
    suspend fun likeArticle(articleId: String): Boolean

    /**
     * Removes a like from an article for the current user
     * @param articleId The ID of the article to unlike
     * @return true if the operation was successful, false otherwise
     */
    suspend fun unlikeArticle(articleId: String): Boolean

    /**
     * Checks if the current user has liked a specific article
     * @param articleId The ID of the article to check
     * @return true if the user has liked the article, false otherwise
     */
    suspend fun isArticleLiked(articleId: String): Boolean
}

