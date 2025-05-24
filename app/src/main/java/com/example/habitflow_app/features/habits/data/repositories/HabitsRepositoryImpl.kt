package com.example.habitflow_app.features.habits.data.repositories

import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.datasources.HabitsDataSource
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest

import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateRequest
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitsDataSource: HabitsDataSource
): HabitsRepository {
    override suspend fun getHabits(userId: String): List<Habit> {
        return habitsDataSource.getHabits(userId)
    }

    override suspend fun createCompleteHabit(request: CreateHabitRequest): HabitResponse {
        return habitsDataSource.createHabit(request)
    }

    override suspend fun updateHabit(habitId: String, request: HabitUpdateRequest): Habit {
        return habitsDataSource.updateHabit(habitId, request)
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

