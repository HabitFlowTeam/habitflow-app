package com.example.habitflow_app.features.profile.di

import com.example.habitflow_app.domain.repositories.ProfileRepository
import com.example.habitflow_app.features.profile.data.repositories.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {
    @Provides
    fun provideProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository = impl
}