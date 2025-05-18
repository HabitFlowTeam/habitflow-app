package com.example.habitflow_app.features.habits.data.repositories

import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.datasources.HabitsDataSource
import com.example.habitflow_app.features.habits.data.dto.HabitRequest
import javax.inject.Inject

class HabitsRepositoryImpl @Inject constructor(
    private val habitsDataSource: HabitsDataSource
): HabitsRepository {
    override suspend fun getHabits(userId: String): List<Habit> {
        return habitsDataSource.getHabits(userId)
    }

    override suspend fun createHabit(request: HabitRequest): Habit {
        return habitsDataSource.createHabit(request)
    }
}