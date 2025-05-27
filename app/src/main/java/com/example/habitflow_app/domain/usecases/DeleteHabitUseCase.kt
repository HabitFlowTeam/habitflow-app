package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.repositories.HabitsRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    suspend operator fun invoke(habitId: String): Boolean {
        return habitsRepository.softDeleteHabit(habitId)
    }

}