package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

class GetUserTopLikedArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(userId: String, top: Int = 2): List<ProfileArticle> {
        return repository.getUserArticles(userId)
            .sortedByDescending { it.likes }
            .take(top)
    }
}