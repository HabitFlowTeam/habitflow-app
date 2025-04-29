package com.example.habitflow_app.core.di

import com.example.habitflow_app.core.validation.FormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidationModule {

    @Provides
    @Singleton
    fun provideFormValidator(): FormValidator {
        return FormValidator
    }
}