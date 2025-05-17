package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.Habit
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
}