package com.example.habitflow_app.features.habits.data.repositories

import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.models.HabitTracking
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.datasources.HabitsDataSource
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayResponse

import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitsDataSource: HabitsDataSource
) : HabitsRepository {

    override suspend fun getUserHabits(): List<ActiveHabitDto> {
        return habitsDataSource.getUserHabits()
    }

    override suspend fun createHabitTracking(
        habitTrackingId: String,
        isChecked: Boolean
    ): HabitTracking {
        return habitsDataSource.createHabitTracking(habitTrackingId, isChecked)
            .toDomainModel()
    }

    override suspend fun updateHabitTrackingCheck(
        habitTrackingId: String,
        isChecked: Boolean
    ): HabitTracking {
        return habitsDataSource.changeHabitCheck(habitTrackingId, isChecked)
            .toDomainModel()
    }

    override suspend fun createCompleteHabit(request: CreateHabitRequest): HabitResponse {
        return habitsDataSource.createHabit(request)
    }

    override suspend fun getHabitDays(habitId: String): List<HabitDayResponse> {
        return habitsDataSource.getHabitDays(habitId)
    }

    override suspend fun updateHabitDays(request: UpdateHabitDaysRequest): HabitUpdateResponse {
        return habitsDataSource.updateHabitDays(request)
    }

    override suspend fun softDeleteHabit(habitId: String): Boolean {
        return habitsDataSource.softDeleteHabit(habitId)
    }

    override suspend fun getCategories(): List<Category> {
        return habitsDataSource.getCategories()
    }

    override suspend fun getCompletedHabitsCount(userId: String): Int {
        return habitsDataSource.getCompletedHabitsCount(userId)
    }
}

