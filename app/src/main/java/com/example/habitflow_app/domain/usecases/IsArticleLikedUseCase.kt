package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

class IsArticleLikedUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(articleId: String): Result<Boolean> =
        runCatching { articleRepository.isArticleLiked(articleId) }
} 