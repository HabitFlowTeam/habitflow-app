package com.example.habitflow_app.features.habits.di

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.datasources.HabitsDataSource
import com.example.habitflow_app.features.habits.data.repositories.HabitsRepositoryImpl
import com.example.habitflow_app.features.habits.validation.HabitFormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitsModule {
    @Provides
    @Singleton
    fun provideHabitsRepository(
        impl: HabitsRepositoryImpl
    ): HabitsRepository = impl

    @Provides
    fun provideHabitsRepositoryImpl(
        dataSource: HabitsDataSource
    ): HabitsRepositoryImpl = HabitsRepositoryImpl(dataSource)

    @Provides
    @Singleton
    fun provideCreateHabitFormValidator(): HabitFormValidator {
        return HabitFormValidator()
    }
}