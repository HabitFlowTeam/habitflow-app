package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.features.habits.data.dto.HabitRequest
import javax.inject.Inject

class HabitsDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
) {
    suspend fun getHabits(userId: String): List<Habit> {
        val response = directusApiService.getHabits(userId)
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Response body is null")
        }
        else {
            throw Exception("Falló algo en algun lado jaja")
        }
    }

    suspend fun createHabit(habitData: HabitRequest): Habit {
        val response = directusApiService.createHabit(habitData)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Failed to create habit: ${response.errorBody()?.string()}")
        }
    }
}