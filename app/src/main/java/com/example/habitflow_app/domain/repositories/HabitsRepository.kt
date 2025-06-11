package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.HabitTracking
import com.example.habitflow_app.domain.models.HabitWithCategory
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse
import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
import java.time.LocalDate

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
    suspend fun getHabitDays(habitId: String): List<HabitDayResponse>
    suspend fun updateHabitDays(request: UpdateHabitDaysRequest): HabitUpdateResponse
    suspend fun softDeleteHabit(habitId: String): Boolean
    suspend fun getCategories(): List<Category>
    suspend fun getCompletedHabitsCount(userId: String): Int
    suspend fun getUserHabitCategoriesView(userId: String): List<HabitWithCategory>

    // New methods for streak management

    /**
     * Gets the scheduled days for a habit (e.g., ["Lunes", "Miércoles", "Viernes"]).
     */
    suspend fun getHabitScheduledDays(habitId: String): List<String>

    /**
     * Updates the streak value for a specific habit.
     */
    suspend fun updateHabitStreak(habitId: String, newStreak: Int): Boolean

    /**
     * Gets the current streak for a habit.
     */
    suspend fun getCurrentHabitStreak(habitId: String): Int


    /**
     * Checks if a habit was completed on a specific date.
     */
    suspend fun wasHabitCompletedOnDate(habitId: String, date: LocalDate): Boolean
}