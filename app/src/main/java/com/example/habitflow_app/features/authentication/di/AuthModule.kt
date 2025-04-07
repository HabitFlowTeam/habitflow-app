package com.example.habitflow_app.features.authentication.di

import com.example.habitflow_app.core.database.SupabaseManager
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.data.datasources.AuthDataSource
import com.example.habitflow_app.features.authentication.data.repositories.AuthRepositoryImpl
import com.example.habitflow_app.features.authentication.validation.RegisterFormValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides authentication-related dependencies.
 * Installed in the SingletonComponent to ensure single instance across the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    /**
     * Provides singleton instance of AuthDataSource.
     *
     * @param supabaseManager Injected Supabase manager instance
     * @return Configured AuthDataSource implementation
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(supabaseManager: SupabaseManager): AuthDataSource {
        return AuthDataSource(supabaseManager)
    }

    /**
     * Provides singleton instance of AuthRepository.
     *
     * @param authDataSource Injected data source instance
     * @return Configured AuthRepository implementation
     */
    @Provides
    @Singleton
    fun provideAuthRepository(authDataSource: AuthDataSource): AuthRepository {
        return AuthRepositoryImpl(authDataSource)
    }

    /**
     * Provides singleton instance of RegisterFormValidator.
     *
     * @return New instance of form validator
     */
    @Provides
    @Singleton
    fun provideRegisterFormValidator(): RegisterFormValidator {
        return RegisterFormValidator()
    }
}
