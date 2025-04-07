package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.User

/**
 * Interface defining the authentication operations for the application.
 */
interface AuthRepository {
    /**
     * Registers a new user in the system.
     * @param user User data to register
     * @return Registered user entity with generated ID
     */
    suspend fun registerUser(user: User): User

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
     * Retrieves the currently authenticated user.
     * @return User entity if authenticated, null otherwise
     */
    suspend fun getCurrentUser(): User?

    /**
     * Checks if there's an active authenticated session.
     * @return true if a user is authenticated, false otherwise
     */
    suspend fun isAuthenticated(): Boolean

    /**
     * Initiates a password reset flow for the given email.
     * @param email Email address associated with the account
     */
    suspend fun resetPassword(email: String)
}
