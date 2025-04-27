package com.example.habitflow_app.core.network

import com.example.habitflow_app.features.authentication.data.dto.LoginRequest
import com.example.habitflow_app.features.authentication.data.dto.LoginResponse
import com.example.habitflow_app.features.authentication.data.dto.RegisterRequest
import com.example.habitflow_app.features.authentication.data.dto.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface defining all API endpoints for Directus backend communication.
 *
 * This interface serves as the contract for all network operations in the application,
 * grouping endpoints by functional areas (authentication, habits, profile, etc.).
 *
 * All methods are suspend functions to support coroutine-based asynchronous execution.
 */
interface DirectusApiService {

    /* Authentication Endpoints */

    /**
     * Registers a new user in the Directus system.
     *
     * @param registerRequest DTO containing user registration data (email, password, etc.)
     * @return RegisterResponse containing the registration result and user details
     */
    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): RegisterResponse

    /**
     * Authenticates an existing user and retrieves access tokens.
     *
     * @param loginDto DTO containing user credentials (email and password)
     * @return LoginResponse containing authentication tokens and user information
     */
    @POST("auth/login")
    suspend fun login(@Body loginDto: LoginRequest): LoginResponse

    /**
     * Invalidates the current user's authentication session.
     * Note: This endpoint might require token invalidation on the server side.
     */
    @POST("auth/logout")
    suspend fun logout()

    /* Gamification Endpoints */
    // TODO: Add gamification-related endpoints as needed

    /* Habits Endpoints */
    // TODO: Add habits-related endpoints as needed

    /* Profile Endpoints */
    // TODO: Add user profile-related endpoints as needed
}
