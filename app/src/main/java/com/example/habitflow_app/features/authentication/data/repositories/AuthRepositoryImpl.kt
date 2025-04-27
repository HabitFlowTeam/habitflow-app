package com.example.habitflow_app.features.authentication.data.repositories

import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.data.datasources.AuthDataSource
import com.example.habitflow_app.features.authentication.data.dto.LoginDto
import com.example.habitflow_app.features.authentication.data.dto.RegisterDto
import javax.inject.Inject

/**
 * Implementation of AuthRepository that mediates between domain layer and data sources. Handles
 * business logic for authentication operations.
 *
 * @property authDataSource The authentication data source
 */
class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) :
        AuthRepository {

    /**
     * Registers a new user by delegating to the data source.
     *
     * @param user User object containing registration details
     * @return Registered User object with complete data
     */
    override suspend fun registerUser(user: User): User {
        return authDataSource.registerUser(
                RegisterDto(
                        email = user.email,
                        password = user.password,
                        fullName = user.fullName,
                        username = user.username
                )
        )
    }

    override suspend fun login(email: String, password: String): User {
        return authDataSource.login(
            LoginDto(
                email = email,
                password = password
            )
        )
    }



    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }

    override suspend fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String) {
        TODO("Not yet implemented")
    }
}
