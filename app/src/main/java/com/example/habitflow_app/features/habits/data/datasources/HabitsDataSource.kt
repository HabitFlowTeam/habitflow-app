package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingResponseDto
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateRequest
import org.json.JSONObject
import java.time.LocalDate
import javax.inject.Inject

class HabitsDataSource @Inject constructor(
    private val directusApiService: DirectusApiService,
    private val authRepository: AuthRepository,
    private val tokenExtractor: ExtractInfoToken
) {
    suspend fun getUserHabits(): List<ActiveHabitDto> {
        val response = directusApiService.getUserHabits()
        return response.habits
    }

    suspend fun changeHabitCheck(
        habitTrackingId: String,
        isChecked: Boolean
    ): HabitTrackingResponseDto {
        Log.d(
            "HabitsDataSource",
            "changeHabitCheck called with trackingId: $habitTrackingId, isChecked: $isChecked"
        )

        try {
            val requestBody = mapOf("is_checked" to isChecked)

            val response = directusApiService.changeHabitCheck(
                habitTrackingId = habitTrackingId,
                request = requestBody
            )

            Log.d("HabitsDataSource", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.d("HabitsDataSource", "Response received: ${responseBody.data}")
                    return responseBody.data
                } else {
                    Log.e("HabitsDataSource", "Response body is null")
                    throw Exception("Empty response body from server")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "HabitsDataSource",
                    "API call failed. Code: ${response.code()}, Error: $errorBody"
                )
                throw Exception("Failed to update habit check: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("HabitsDataSource", "Exception in changeHabitCheck", e)
            throw Exception("Network error while updating habit: ${e.message}", e)
        }
    }

    suspend fun createHabit(request: CreateHabitRequest): HabitResponse {
        // 1. Authentication and obtaining userId
        val userId = getAuthenticatedUserId()

        Log.d("FLOW", "Step 1 - Start")
        val habitId = createHabitInApi(request, userId)
        Log.d("FLOW", "Step 2 - Habit created: $habitId")

        // 3. Create associated days
        createHabitDaysInApi(habitId, request.selectedDays)
        Log.d("FLOW", "Step 3 - Days created")

        // 4. Create initial tracking
        if (request.initialTracking) {
            createHabitTracking(habitId)
            Log.d("FLOW", "Step 4 - Tracking created")
        }

        // We return the data of the created habit
        return HabitResponse(
            name = request.name,
            categoryId = request.categoryId,
            selectedDays = request.selectedDays,
            reminderTime = request.reminderTime
        )
    }

    private suspend fun getAuthenticatedUserId(): String {
        val token = authRepository.getAccessTokenOnce() ?: throw Exception()
        return tokenExtractor.extractUserIdFromToken(token)
    }

    private suspend fun createHabitInApi(request: CreateHabitRequest, userId: String): String {
        val response = directusApiService.createHabit(
            HabitApiRequest(
                name = request.name,
                userId = userId,
                categoryId = request.categoryId,
                reminderTime = request.reminderTime?.toString(),
                notificationsEnabled = request.notificationsEnabled
            )
        )

        if (!response.isSuccessful) {
            throw Exception("Error creating habit: ${response.errorBody()?.string()}")
        }

        val responseBody = response.body()?.string()
        return try {
            JSONObject(responseBody ?: "").getJSONObject("data").getString("id").also {
                Log.d("API", "Extracted habit ID: $it")
            }
        } catch (e: Exception) {
            Log.e("API", "Failed to parse response: $responseBody", e)
            throw Exception("Failed to parse habit ID from response")
        }
    }

    private suspend fun createHabitDaysInApi(habitId: String, days: List<String>) {
        val results = days.map { dayId ->
            val response = directusApiService.createHabitDay(HabitDayApiRequest(habitId, dayId))

            if (!response.isSuccessful) {
                Log.d(">>>", "error in createHabitDaysInApi ")
            }

            response.isSuccessful
        }

        if (results.any { !it }) {
            throw Exception("Failed to create some habit days")
        }
    }

    suspend fun createHabitTracking(
        habitId: String,
        isChecked: Boolean = false // Default to false for initial tracking
    ): HabitTrackingResponseDto {
        val response = directusApiService.createHabitTracking(
            HabitTrackingApiRequest(
                isChecked = isChecked,
                trackingDate = LocalDate.now().toString(),
                habitId = habitId
            )
        )

        if (response.isSuccessful) {
            return response.body()?.data ?: throw Exception("Empty response")
        } else {
            throw Exception("Failed to create habit tracking: ${response.errorBody()?.string()}")
        }
    }

    suspend fun updateHabit(habitId: String, request: HabitUpdateRequest): Habit {
        val response = directusApiService.updateHabit(habitId, request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception("Failed to update habit: ${response.errorBody()?.string()}")
        }
    }

    suspend fun softDeleteHabit(habitId: String): Boolean {
        val response = directusApiService.softDeleteHabit(habitId)
        if (response.isSuccessful) {
            return response.body()?.isDeleted == true
        } else {
            throw Exception("Failed to delete habit: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getCategories(): List<Category> {
        val response = directusApiService.getCategories()
        if (response.isSuccessful) {
            return response.body()?.data?.map {
                Category(
                    id = it.id,
                    name = it.name,
                    description = it.description
                )
            } ?: emptyList()
        } else {
            throw Exception("Failed to fetch categories: ${response.errorBody()?.string()}")
        }
    }

    suspend fun getCompletedHabitsCount(userId: String): Int {
        val response = directusApiService.getCompletedHabitsTracking(userId)
        if (response.isSuccessful) {
            return response.body()?.data?.size ?: 0
        } else {
            throw Exception("Error getting completed habits")
        }
    }

}

