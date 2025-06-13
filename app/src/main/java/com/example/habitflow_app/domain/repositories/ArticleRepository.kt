package com.example.habitflow_app.domain.repositories

import android.content.Context
import android.net.Uri
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

    /**
     * Creates a new article
     * @param title The title of the article
     * @param content The content of the article
     * @param imageUri Optional URI of the image to upload
     * @param categoryId The ID of the category the article belongs to
     * @param context Android context for image processing
     * @return The ID of the created article
     */
    suspend fun createArticle(
        title: String,
        content: String,
        imageUri: Uri?,
        categoryId: String,
        context: Context
    ): String
}

