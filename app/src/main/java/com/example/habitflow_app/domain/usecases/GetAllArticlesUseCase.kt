package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.RankedArticle
import com.example.habitflow_app.domain.repositories.ArticleRepository
import javax.inject.Inject

class GetAllArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    /**
     * Ejecuta el caso de uso para obtener todos los artículos.
     * @return Lista de [RankedArticle] sin filtrar ni ordenar.
     */
    suspend operator fun invoke(): List<RankedArticle> {
        return repository.getRankedArticles()
    }
}

