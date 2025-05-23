package com.example.habitflow_app.core.network

import com.example.habitflow_app.features.authentication.data.dto.LoginRequest
import com.example.habitflow_app.features.authentication.data.dto.LoginResponse
import com.example.habitflow_app.features.authentication.data.dto.RegisterUserRequest
import com.example.habitflow_app.features.authentication.data.dto.CreateProfileRequest
import com.example.habitflow_app.features.authentication.data.dto.PasswordResetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import com.example.habitflow_app.features.profile.data.dto.ProfileDTO
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
     * This endpoint returns a 204 No Content response on success.
     *
     * @param registerRequest DTO containing user registration data (email, password, etc.)
     * @return Retrofit Response object to check for status codes
     */
    @POST("users")
    suspend fun registerUser(@Body registerRequest: RegisterUserRequest): Response<Void>

    /**
     * Delete a user in the Directus system.
     * This endpoint returns a 204 No Content response on success.
     *
     * @param id The ID of the user to delete
     * @return Retrofit Response object to check for status codes
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Void>

    /**
     * Creates a new profile in the Directus system.
     *
     * @param createProfileRequest DTO containing profile registration data (fullName, username, etc.)
     * @return Retrofit Response object to check for status codes
     */
    @POST("items/profiles")
    suspend fun createProfile(@Body createProfileRequest: CreateProfileRequest): Response<Void>

    /**
     * Authenticates an existing user and retrieves access tokens.
     *
     * @param loginDto DTO containing user credentials (email and password)
     * @return LoginResponse containing authentication tokens and user information
     */
    @POST("auth/login")
    suspend fun login(@Body loginDto: LoginRequest): LoginResponse

    @POST("auth/password/request")
    suspend fun resetPassword(
        @Body request: PasswordResetRequest
    ): Response<Unit>

    /**
     * Invalidates the current user's authentication session.
     * Note: This endpoint might require token invalidation on the server side.
     */
    @POST("auth/logout")
    suspend fun logout()

    @GET("items/profiles/{id}")
    suspend fun getProfile(@Path("id") userId: String): Response<ProfileResponse>

    data class ProfileResponse(val data: ProfileDTO)

    /* Gamification Endpoints */
    // TODO: Add gamification-related endpoints as needed

    /* Habits Endpoints */
    // TODO: Add habits-related endpoints as needed

    /* Profile Endpoints */
    // TODO: Add user profile-related endpoints as needed
}
