package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the authentication operations for the application.
 */
interface AuthRepository {
    /**
     * Registers a new user in the system.
     * @param user User data to register
     * @param profile User data to create
     * @return Registered user entity with generated ID
     */
    suspend fun registerUser(user: User, profile: Profile): User

    /**
     * Authenticates a user with email and password credentials.
     * @param email User's email address
     * @param password User's password
     * @return Authenticated user entity
     */
    suspend fun login(email: String, password: String): User

    /**
     * Terminates the current user session.
     * Clears any authentication tokens or cached user data.
     */
    suspend fun logout()

    /**
     * Initiates a password reset flow for the given email.
     * @param email Email address associated with the account
     */
    suspend fun resetPassword(email: String)

    /**
     * Retrieves the current access token for the authenticated user.
     * This token is used for making authenticated API requests.
     */
    fun getAccessToken(): Flow<String?>

    suspend fun getAccessTokenOnce(): String?
}
