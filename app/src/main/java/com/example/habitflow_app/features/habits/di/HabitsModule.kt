package com.example.habitflow_app.features.habits.di

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.authentication.validation.RegisterFormValidator
import com.example.habitflow_app.features.habits.data.repositories.HabitsRepositoryImpl
import com.example.habitflow_app.features.habits.validation.HabitFormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitsModule {
    @Provides
    fun provideHabitsRepository(
        impl: HabitsRepositoryImpl
    ): HabitsRepository = impl

    @Provides
    @Singleton
    fun provideCreateHabitFormValidator(): HabitFormValidator {
        return HabitFormValidator()
    }
}