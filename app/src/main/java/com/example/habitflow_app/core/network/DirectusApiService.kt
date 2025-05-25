package com.example.habitflow_app.core.network

import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.features.authentication.data.dto.LoginRequest
import com.example.habitflow_app.features.authentication.data.dto.LoginResponse
import com.example.habitflow_app.features.authentication.data.dto.RegisterUserRequest
import com.example.habitflow_app.features.authentication.data.dto.CreateProfileRequest
import com.example.habitflow_app.features.authentication.data.dto.PasswordResetRequest
import com.example.habitflow_app.features.category.data.dto.CategoriesResponse
import com.example.habitflow_app.features.gamification.data.dto.HabitRankingResponse
import com.example.habitflow_app.features.gamification.data.dto.LeaderboardResponse
import com.example.habitflow_app.features.gamification.data.dto.ProfileRankingResponse
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingApiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import com.example.habitflow_app.features.profile.data.dto.ProfileDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface defining all API endpoints for Directus backend communication.
 *
 * This interface serves as the contract for all network operations in the application,
 * grouping endpoints by functional areas (authentication, habits, profile, etc.).
 *
 * All methods are suspend functions to support coroutine-based asynchronous execution.
 */
interface DirectusApiService {

    data class ListResponse<T>(val data: List<T>)
    data class SingleResponse<T>(val data: T)

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

    /* Habit Endpoints */

    /**
     * Retrieves all habits for a specific user.
     *
     * @param userId The ID of the user to filter habits by
     * @return Response containing a list of Habit objects for the specified user
     */
    @GET("items/habits")
    suspend fun getHabits(@Query("filter[user_id][_eq]") userId: String): Response<List<Habit>>

    /**
     * Creates a new habit in the system.
     *
     * @param habitDTO The HabitApiRequest object containing habit details
     * @return Response with the created habit data in the body
     */
    @POST("items/habits")
    suspend fun createHabit(@Body habitDTO: HabitApiRequest): Response<ResponseBody>

    /**
     * Creates a new habit-day association in the system.
     *
     * @param request The HabitDayApiRequest object containing habit-day relationship details
     * @return Response with the created habit-day association data in the body
     */
    @POST("items/habits_days")
    suspend fun createHabitDay(@Body request: HabitDayApiRequest): Response<ResponseBody>

    /**
     * Creates a new habit tracking record.
     *
     * @param request The HabitTrackingApiRequest object containing tracking details
     * @return Response with status of the operation (no content body expected)
     */
    @POST("items/habits_tracking")
    suspend fun createHabitTracking(@Body request: HabitTrackingApiRequest): Response<Unit>

    /**
     * Retrieves all day associations for a specific habit.
     *
     * @param habitId The ID of the habit to get days for
     * @param fields Comma-separated list of fields to return (defaults to habit_id and week_day_id)
     * @return Response containing a list of HabitDayResponse objects
     */
    @GET("items/habits_days")
    suspend fun getHabitDays(
        @Query("filter[habit_id][_eq]") habitId: String,
        @Query("fields") fields: String = "habit_id,week_day_id"
    ): Response<List<HabitDayResponse>>

    /**
     * Deletes all day associations for a specific habit.
     *
     * @param habitId The ID of the habit whose day associations should be deleted
     * @return Response with status of the operation (no content body expected)
     */
    @DELETE("items/habits_days")
    suspend fun deleteHabitDays(
        @Query("filter[habit_id][_eq]") habitId: String
    ): Response<Unit>

    /**
     * Soft deletes a habit by marking it as deleted (is_deleted = true).
     *
     * @param habitId The ID of the habit to soft delete
     * @param request Map containing the soft delete flag (defaults to {"is_deleted": true})
     * @return Response containing the updated Habit object
     */
    @PATCH("items/habits/{habit_id}")
    suspend fun softDeleteHabit(
        @Path("habit_id") habitId: String,
        @Body request: Map<String, Boolean> = mapOf("is_deleted" to true)
    ): Response<Habit>

    /* Gamification Endpoints */

    /**
     * Retrieves the global leaderboard ranking sorted by streak in descending order.
     *
     * This endpoint returns profile information including id, full name, streak count, and avatar URL
     * for the top users with the highest streaks.
     *
     * @param sort Sorting criteria (default: "-streak" for descending order by streak)
     * @param limit Maximum number of results to return (default: 10)
     * @param fields Comma-separated list of fields to include in the response
     * @return Retrofit Response containing the leaderboard data wrapped in LeaderboardResponse
     */
    @GET("items/profiles")
    suspend fun getGlobalRanking(
        @Query("sort") sort: String = "-streak",
        @Query("limit") limit: Int = 10,
        @Query("fields") fields: String = "id,full_name,streak,avatar_url"
    ): Response<LeaderboardResponse>

    /**
     * Retrieves the category-specific leaderboard ranking for habits.
     *
     * This endpoint returns user IDs and streak counts for habits matching the specified category,
     * filtered to exclude deleted habits and sorted by streak in descending order.
     *
     * @param categoryName Exact name of the category to filter by
     * @param isDeleted Filter for deleted habits (default: false)
     * @param sort Sorting criteria (default: "-streak" for descending order by streak)
     * @param limit Maximum number of results to return (default: 10)
     * @param fields Comma-separated list of fields to include in the response
     * @return Retrofit Response containing habit ranking data wrapped in HabitRankingResponse
     */
    @GET("items/habits")
    suspend fun getCategoryRanking(
        @Query("filter[category_id][name][_eq]") categoryName: String,
        @Query("filter[is_deleted][_eq]") isDeleted: Boolean = false,
        @Query("sort") sort: String = "-streak",
        @Query("limit") limit: Int = 10,
        @Query("fields") fields: String = "user_id,streak"
    ): Response<HabitRankingResponse>

    /**
     * Retrieves profile information for multiple users by their IDs.
     *
     * This endpoint is typically used to fetch additional user details (full name, avatar)
     * after obtaining user IDs from the category ranking endpoint.
     *
     * @param userIds Comma-separated list of user IDs to retrieve
     * @param fields Comma-separated list of fields to include in the response
     * @return Retrofit Response containing profile data wrapped in ProfileRankingResponse
     */
    @GET("items/profiles")
    suspend fun getProfileRanking(
        @Query("filter[id][_in]") userIds: String,
        @Query("fields") fields: String = "id,full_name,avatar_url"
    ): Response<ProfileRankingResponse>

    /* Habits Endpoints */
    // TODO: Add habits-related endpoints as needed

    /* Profile Endpoints */
    // TODO: Add user profile-related endpoints as needed

    /* Category Endpoint */
    /**
     * Retrieves all available habit categories with their names.
     *
     * This endpoint returns a list of all categories stored in the system,
     * containing only their names for lightweight listing purposes.
     * Results are sorted alphabetically by category name in ascending order.
     *
     * @param fields Comma-separated list of fields to include (default: "name")
     * @param sort Sorting criteria (default: "name" for alphabetical order)
     * @return Retrofit Response containing the list of categories
     */
    @GET("items/categories")
    suspend fun getCategories(): Response<CategoriesResponse>
}
