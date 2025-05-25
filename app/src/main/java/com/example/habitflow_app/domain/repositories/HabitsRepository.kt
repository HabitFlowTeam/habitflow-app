package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest

interface HabitsRepository {
    suspend fun getHabits(userId: String): List<Habit>
    suspend fun createCompleteHabit(request: CreateHabitRequest): HabitResponse
    suspend fun getHabitDays(habitId: String): List<HabitDayResponse>
    suspend fun updateHabitDays(request: UpdateHabitDaysRequest): HabitUpdateResponse
    suspend fun softDeleteHabit(habitId: String): Boolean
    suspend fun getCategories(): List<Category>
}