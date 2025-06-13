package com.example.habitflow_app.domain.usecases

import android.content.Context
import android.net.Uri
import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

class CreateArticleUseCase @Inject constructor(
    private val articleRepository: ArticleRepository
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        imageUri: Uri?,
        categoryId: String,
        context: Context
    ): Result<String> = runCatching {
        articleRepository.createArticle(
            title = title,
            content = content,
            imageUri = imageUri,
            categoryId = categoryId,
            context = context
        )
    }
}