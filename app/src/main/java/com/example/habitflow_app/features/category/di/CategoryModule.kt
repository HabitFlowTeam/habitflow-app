package com.example.habitflow_app.features.category.di

import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.repositories.CategoryRepository
import com.example.habitflow_app.features.category.data.datasources.CategoryDataSource
import com.example.habitflow_app.features.category.data.repositories.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
/**
 * Dagger Hilt module that provides dependencies for category-related data sources and repositories.
 */
object CategoryModule {

    /**
     * Provides a singleton instance of [CategoryDataSource].
     *
     * @param directusApiService The API service for Directus backend communication.
     * @return An instance of [CategoryDataSource].
     */
    @Provides
    @Singleton
    fun provideCategoryDataSource(
        directusApiService: DirectusApiService
    ): CategoryDataSource {
        return CategoryDataSource(directusApiService)
    }

    /**
     * Provides a singleton instance of [CategoryRepository].
     *
     * @param categoryDataSource The data source for category data.
     * @return An implementation of [CategoryRepository].
     */
    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDataSource: CategoryDataSource
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDataSource)
    }

}
