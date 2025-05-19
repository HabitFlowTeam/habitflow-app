package com.example.habitflow_app.features.habits.di

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.repositories.HabitsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HabitsModule {
    @Provides
    fun provideHabitsRepository(
        impl: HabitsRepositoryImpl
    ): HabitsRepository = impl
}