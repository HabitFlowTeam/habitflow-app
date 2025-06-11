package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import javax.inject.Inject

/**
 * Encapsulates the business logic for create habit.
 *
 * @property habitsRepository The habits data source
 */
class CreateHabitUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    suspend operator fun invoke(request: CreateHabitRequest): HabitResponse {
        val result = habitsRepository.createCompleteHabit(request)
        return result
    }
}