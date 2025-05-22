package com.example.habitflow_app.features.category.data.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.repositories.CategoryRepository
import com.example.habitflow_app.features.category.data.datasources.CategoryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of [CategoryRepository] that interacts with the data source to fetch category data.
 * Supports both direct fetching and observing categories as a flow.
 *
 * @property categoryDataSource The data source used to retrieve category data.
 */
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDataSource: CategoryDataSource
) : CategoryRepository {

    /**
     * Fetches the list of categories from the data source.
     *
     * @return List of [Category] objects.
     */
    override suspend fun getCategories(): List<Category> {
        return categoryDataSource.getCategories()
    }

    /**
     * Observes the list of categories as a [Flow].
     *
     * @return A [Flow] emitting lists of [Category] objects.
     */
    override fun observeCategories(): Flow<List<Category>> = flow {
        emit(getCategories())
    }
}
