package com.example.habitflow_app.features.articles.data.repositories

import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import com.example.habitflow_app.features.articles.data.datasources.ArticleDataSource
import com.example.habitflow_app.features.articles.data.dto.ProfileArticlesDTO
import javax.inject.Inject

/**
 * Repository implementation for article-related operations.
 *
 * This class acts as a bridge between the data source and the domain layer,
 * providing methods to retrieve articles for a user.
 *
 * @property articleDataSource The data source used to fetch article data
 */
class ArticleRepositoryImpl @Inject constructor(
    private val articleDataSource: ArticleDataSource
): ArticleRepository {
    /**
     * Retrieves the list of articles for a specific user.
     * @param userId The ID of the user whose articles are to be fetched
     * @return List of [ProfileArticle] objects
     */
    override suspend fun getUserArticles(userId: String): List<ProfileArticle> {
        return articleDataSource.getUserArticles(userId)
    }
}

