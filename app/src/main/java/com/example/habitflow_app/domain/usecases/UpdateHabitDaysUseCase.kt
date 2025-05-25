package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
import javax.inject.Inject

/**
 * Encapsulates the business logic to edit the days of a habit.
 *
 * @property habitsRepository The habits data source
 */
class UpdateHabitDaysUseCase @Inject constructor(
    private val repository: HabitsRepository
) {
    suspend operator fun invoke(habitId: String, days: List<String>): Result<HabitUpdateResponse> {
        return try {
            val response = repository.updateHabitDays(UpdateHabitDaysRequest(habitId, days))

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se pudieron actualizar todos los días"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}