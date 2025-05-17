package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.Habit

interface HabitsRepository {
    suspend fun getHabits(userId: String): List<Habit>
}