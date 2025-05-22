package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Category
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the operations for category data access and observation.
 */
interface CategoryRepository {
    /**
     * Retrieves the list of all categories.
     * @return List of [Category] objects.
     * @throws Exception if there's an error fetching the categories.
     */
    suspend fun getCategories(): List<Category>

    /**
     * Observes changes in the categories list over time.
     * @return [Flow] emitting lists of [Category] whenever they change.
     */
    fun observeCategories(): Flow<List<Category>>
}
