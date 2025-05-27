package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.RankedArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

class GetRankedArticles @Inject constructor(
    private val repository: ArticleRepository
) {
    /**
     * Executes the use case to retrieve the ranked articles.
     * @param top The maximum number of ranked articles to return (default: 2)
     * @return List of [RankedArticle] ordered by number of likes in descending order
     */
    suspend operator fun invoke(top: Int = 5): List<RankedArticle> {
        return repository.getRankedArticles()
            .sortedByDescending { it.likesCount }
            .take(top)
    }
}