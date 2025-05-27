package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.repositories.CategoryRepository
import javax.inject.Inject

/**
 * Encapsulates the business logic for retrieving categories.
 *
 * @property categoryRepository The category data source
 */
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    /**
     * Executes the use case to retrieve all available categories.
     * @return List of [Category] entities
     */
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getCategories()
    }
}
