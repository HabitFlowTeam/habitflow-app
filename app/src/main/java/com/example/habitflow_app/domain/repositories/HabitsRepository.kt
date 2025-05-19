package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.features.habits.data.dto.HabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateRequest

interface HabitsRepository {
    suspend fun getHabits(userId: String): List<Habit>
    suspend fun createHabit(request: HabitRequest): Habit
    suspend fun updateHabit(habitId: String, request: HabitUpdateRequest): Habit
    suspend fun softDeleteHabit(habitId: String): Boolean
    suspend fun getCategories(): List<Category>
}