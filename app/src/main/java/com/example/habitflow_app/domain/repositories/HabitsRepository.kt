package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.features.habits.data.dto.HabitRequest

interface HabitsRepository {
    suspend fun getHabits(userId: String): List<Habit>
    suspend fun createHabit(request: HabitRequest): Habit
}