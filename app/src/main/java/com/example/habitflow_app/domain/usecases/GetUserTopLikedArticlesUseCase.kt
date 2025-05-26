package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

/**
 * Encapsulates the business logic for retrieving a user's top liked articles.
 *
 * @property repository The article data source
 */
class GetUserTopLikedArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    /**
     * Executes the use case to retrieve the top liked articles for a user.
     * @param userId The ID of the user whose articles are to be fetched
     * @param top The maximum number of top liked articles to return (default: 2)
     * @return List of [ProfileArticle] ordered by number of likes in descending order
     */
    suspend operator fun invoke(userId: String, top: Int = 2): List<ProfileArticle> {
        return repository.getUserArticles(userId)
            .sortedByDescending { it.likes }
            .take(top)
    }
}

