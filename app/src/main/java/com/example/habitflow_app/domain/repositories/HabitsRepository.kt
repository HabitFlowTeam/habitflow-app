package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.models.HabitTracking
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateRequest

interface HabitsRepository {
    suspend fun getUserHabits(): List<ActiveHabitDto>
    suspend fun createHabitTracking(
        habitTrackingId: String,
        isChecked: Boolean
    ): HabitTracking

    suspend fun updateHabitTrackingCheck(
        habitTrackingId: String,
        isChecked: Boolean
    ): HabitTracking

    suspend fun createCompleteHabit(request: CreateHabitRequest): HabitResponse
    suspend fun updateHabit(habitId: String, request: HabitUpdateRequest): Habit
    suspend fun softDeleteHabit(habitId: String): Boolean
    suspend fun getCategories(): List<Category>
    suspend fun getCompletedHabitsCount(userId: String): Int
}

