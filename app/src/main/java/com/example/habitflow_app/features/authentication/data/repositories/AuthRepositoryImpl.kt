package com.example.habitflow_app.features.authentication.data.repositories

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.data.datasources.AuthDataSource
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataSourceProvider
import com.example.habitflow_app.features.authentication.data.dto.LoginRequest
import javax.inject.Inject

/**
 * Implementation of AuthRepository that mediates between domain layer and data sources.
 * Handles business logic for authentication operations.
 *
 * @property authDataSource The authentication data source
 */
class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
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
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAccessToken(): String? {
        var accessToken: String? = null
        LocalDataSourceProvider.getInstance().load("access_token").collect { token ->
            accessToken = token
        }
        return accessToken
    }
}