package com.example.habitflow_app.features.habits.di

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.domain.repositories.ProfileRepository
import com.example.habitflow_app.domain.usecases.DeleteHabitUseCase
import com.example.habitflow_app.domain.usecases.GetUserHabitCategoriesUseCase
import com.example.habitflow_app.domain.usecases.StreakManagementUseCase
import com.example.habitflow_app.features.habits.data.datasources.HabitsDataSource
import com.example.habitflow_app.features.habits.data.repositories.HabitsRepositoryImpl
import com.example.habitflow_app.features.habits.validation.EditHabitFormValidator
import com.example.habitflow_app.features.habits.validation.HabitFormValidator
import com.example.habitflow_app.features.profile.data.repositories.ProfileRepositoryImpl
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

    @Provides
    @Singleton
    fun provideHabitEditFormValidator(): EditHabitFormValidator {
        return EditHabitFormValidator()
    }

    @Provides
    @Singleton
    fun provideSoftDeleteHabitUseCase(repository: HabitsRepository): DeleteHabitUseCase {
        return DeleteHabitUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserHabitCategoriesUseCase(repository: HabitsRepository): GetUserHabitCategoriesUseCase {
        return GetUserHabitCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository = impl

    /**
     * Provides the simplified streak management use case that doesn't depend on complex APIs.
     */
    @Provides
    @Singleton
    fun provideStreakManagementUseCase(
        habitsRepository: HabitsRepository
    ): StreakManagementUseCase {
        return StreakManagementUseCase(habitsRepository)
    }

}