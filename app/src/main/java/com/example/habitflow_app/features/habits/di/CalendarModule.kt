package com.example.habitflow_app.features.habits.di

import com.example.habitflow_app.domain.repositories.CalendarRepository
import com.example.habitflow_app.domain.datasources.CalendarDataSource
import com.example.habitflow_app.features.habits.data.repositories.CalendarRepositoryImpl
import com.example.habitflow_app.features.habits.data.datasources.CalendarDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing dependencies related to the calendar feature.
 * This module binds the CalendarRepository and CalendarDataSource interfaces
 * to their respective implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    /**
     * Binds the CalendarRepositoryImpl to the CalendarRepository interface.
     * This allows for dependency injection of the repository implementation.
     *
     * @param impl The implementation of the CalendarRepository interface.
     * @return The bound CalendarRepository instance.
     */
    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        impl: CalendarRepositoryImpl
    ): CalendarRepository

    /**
     * Binds the CalendarDataSourceImpl to the CalendarDataSource interface.
     * This allows for dependency injection of the data source implementation.
     *
     * @param impl The implementation of the CalendarDataSource interface.
     * @return The bound CalendarDataSource instance.
     */
    @Binds
    @Singleton
    abstract fun bindCalendarDataSource(
        impl: CalendarDataSourceImpl
    ): CalendarDataSource
}
