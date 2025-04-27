package com.example.habitflow_app.features.authentication.data.datasources

import com.example.habitflow_app.core.database.SupabaseManager
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.features.authentication.data.dto.LoginDto
import com.example.habitflow_app.features.authentication.data.dto.RegisterDto
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import java.time.Instant
import javax.inject.Inject

/**
 * Data source implementation for authentication operations using Supabase. Handles direct
 * communication with Supabase services for auth-related operations.
 */
class AuthDataSource @Inject constructor(private val supabaseManager: SupabaseManager) {

    /**
     * Registers a new user in both Supabase Auth and custom users table.
     *
     * @param registerDto Data transfer object containing user registration details
     * @return Registered User object if successful
     * @throws Exception if registration fails at any step
     */
    suspend fun registerUser(registerDto: RegisterDto): User {
        // Register user in Supabase Authentication service
        supabaseManager.client.auth.signUpWith(Email) {
            email = registerDto.email
            password = registerDto.password
        }

        // Retrieve the newly created user's UID
        val currentUser =
                supabaseManager.client.auth.currentUserOrNull()
                        ?: throw Exception(
                                "Error en el registro del usuario: no se encontró ningún usuario autenticado"
                        )

        // Insert user profile data into custom 'users' table
        supabaseManager
                .client
                .from("users")
                .insert(
                        mapOf(
                                "id" to currentUser.id,
                                "fullname" to registerDto.fullName,
                                "username" to registerDto.username,
                                "email" to registerDto.email,
                        )
                )

        return User(
                id = currentUser.id,
                fullName = registerDto.fullName,
                username = registerDto.username,
                email = registerDto.email,
                password = "",
                streak = 0,
                avatarUrl = null,
                createdAt = Instant.now()
        )
    }

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param email User's email address
     * @param password User's password
     * @return Authenticated [User] object
     */
    suspend fun login(loginDto: LoginDto): User {
        TODO("Implement login functionality")
    }

    /** Terminates the current authenticated session. */
    suspend fun logout() {
        TODO("Implement logout functionality")
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return [User] object if authenticated, null otherwise
     */
    suspend fun getCurrentUser(): User? {
        TODO("Implement get current user functionality")
    }

    /**
     * Checks if there's an active authenticated session.
     *
     * @return Boolean indicating authentication status
     */
    suspend fun isAuthenticated(): Boolean {
        TODO("Implement authentication check")
    }

    /**
     * Initiates password reset flow for the given email.
     *
     * @param email Email address associated with the account
     */
    suspend fun resetPassword(email: String) {
        TODO("Implement password reset functionality")
    }
}
