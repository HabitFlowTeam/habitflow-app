package com.example.habitflow_app.features.authentication.data.repositories

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.data.datasources.AuthDataSource
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import com.example.habitflow_app.features.authentication.data.dto.LoginRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of AuthRepository that mediates between domain layer and data sources.
 * Handles business logic for authentication operations.
 *
 * @property authDataSource The authentication data source
 */
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val localDataStore: LocalDataStore
) :
    AuthRepository {

    /**
     * Registers a new user by delegating to the data source.
     *
     * @param user User object containing registration details
     * @param profile Profile object containing user profile details
     * @return Registered User object with complete data
     */
    override suspend fun registerUser(user: User, profile: Profile): User {
        return authDataSource.registerUser(user, profile)
    }

    override suspend fun login(email: String, password: String): User {
        return authDataSource.login(LoginRequest(email = email, password = password))
    }

    override suspend fun logout() {
        authDataSource.logout()
    }

    override suspend fun requestPasswordReset(email: String) {
        authDataSource.requestPasswordReset(email)
    }

    override fun getAccessToken(): Flow<String?> {
        return localDataStore.getAccessToken()
    }

    override suspend fun getAccessTokenOnce(): String? {
        return localDataStore.getAccessTokenOnce()
    }
}