package com.example.habitflow_app.features.authentication.di

import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.data.datasources.AuthDataSource
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import com.example.habitflow_app.features.authentication.data.repositories.AuthRepositoryImpl
import com.example.habitflow_app.features.authentication.validation.EmailValidator
import com.example.habitflow_app.features.authentication.validation.LoginFormValidator
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
     * @param directusApiService Injected Directus API service
     * @param extractInfoToken Injected Extract info token service
     * @return Configured AuthDataSource implementation
     */
    @Provides
    @Singleton
    fun provideAuthDataSource(
        directusApiService: DirectusApiService,
        extractInfoToken: ExtractInfoToken,
        localDataStore: LocalDataStore
    ): AuthDataSource {
        return AuthDataSource(directusApiService, extractInfoToken, localDataStore)
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

    /**
     * Provides singleton instance of LoginFormValidator.
     *
     * @return New instance of form validator
     */
    @Provides
    @Singleton
    fun provideLoginFormValidator(): LoginFormValidator {
        return LoginFormValidator()
    }

    /**
     * Provides singleton instance of AuthRepository.
     *
     * @param authDataSource Injected data source instance
     * @param localDataStore Injected local data store instance
     * @return Configured AuthRepository implementation
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        authDataSource: AuthDataSource,
        localDataStore: LocalDataStore
    ): AuthRepository {
        return AuthRepositoryImpl(authDataSource, localDataStore)
    }

    @Provides
    @Singleton
    fun provideEmailValidator(): EmailValidator {
        return EmailValidator()
    }
}
