package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.network.DirectusApiService.DeleteHabitDaysRequest
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingApiRequest
import com.example.habitflow_app.features.habits.data.dto.HabitTrackingResponseDto
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
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

    suspend fun getHabitDays(habitId: String): List<HabitDayResponse> {
        val response = directusApiService.getHabitDays(habitId)
        if (!response.isSuccessful) {
            throw Exception("Error getting habit days: ${response.errorBody()?.string()}")
        }
        return response.body()?.data ?: emptyList()
    }

    fun String.normalizeUUID(): String {
        return if (this.length == 32 && !this.contains("-")) {
            "${this.substring(0, 8)}-${this.substring(8, 12)}-${this.substring(12, 16)}-" +
                    "${this.substring(16, 20)}-${this.substring(20)}"
        } else {
            this
        }
    }

    suspend fun updateHabitDays(request: UpdateHabitDaysRequest): HabitUpdateResponse {
        val normalizedHabitId = request.habitId.normalizeUUID()
        Log.d("HabitsDataSource", "Updating days for habit: $normalizedHabitId")

        try {

            val habitResponse = directusApiService.getHabitById(normalizedHabitId)
            if (!habitResponse.isSuccessful || habitResponse.body()?.data.isNullOrEmpty()) {
                throw Exception("Habit with ID $normalizedHabitId does not exist")
            }
            Log.d("HabitsDataSource", "Habit exists, proceeding with day updates")
            
            // 1. Obtener días existentes para este hábito
            Log.d("HabitsDataSource", "Fetching existing days...")
            val existingDays = getHabitDays(normalizedHabitId)

            // 2. Eliminar días existentes usando sus IDs
            if (existingDays.isNotEmpty()) {
                Log.d("HabitsDataSource", "Deleting ${existingDays.size} existing days...")
                val idsToDelete = existingDays.map { it.id }
                Log.d("DELETE_REQUEST", "Deleting IDs: ${idsToDelete.joinToString()}")
                val deleteResponse = directusApiService.deleteHabitDays(DeleteHabitDaysRequest(idsToDelete))

                if (!deleteResponse.isSuccessful) {
                    val errorBody = deleteResponse.errorBody()?.string()
                    Log.e("HabitsDataSource", "Delete failed. Code: ${deleteResponse.code()}, Error: $errorBody")
                    throw Exception("Failed to delete existing days: $errorBody")
                }
                Log.d("HabitsDataSource", "Existing days deleted successfully")
            } else {
                Log.d("HabitsDataSource", "No existing days to delete")
            }

            // 3. Crear nuevos días
            Log.d("HabitsDataSource", "Creating ${request.days.size} new days...")
            val createdCount = request.days.mapIndexed { index, dayId ->
                val normalizedDayId = dayId.normalizeUUID()
                Log.d("HabitsDataSource", "Creating day ${index + 1}: $normalizedDayId")
                val response = directusApiService.createHabitDay(
                    HabitDayApiRequest(normalizedHabitId, normalizedDayId)
                )
                if (!response.isSuccessful) {
                    Log.e("HabitsDataSource", "Failed to create day $normalizedDayId: ${response.errorBody()?.string()}")
                }
                response.isSuccessful
            }.count { it }

            Log.d("HabitsDataSource", "Successfully created $createdCount/${request.days.size} days")
            return HabitUpdateResponse(
                success = createdCount == request.days.size,
                updatedCount = createdCount
            )
        } catch (e: Exception) {
            Log.e("HabitsDataSource", "Error in updateHabitDays", e)
            throw Exception("Failed to update habit days: ${e.message}")
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