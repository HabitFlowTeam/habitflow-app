package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User

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
     * Sends a password reset email to the specified address
     * @param email Email address to send reset instructions to
     */
    suspend fun sendPasswordResetEmail(email: String)
}
