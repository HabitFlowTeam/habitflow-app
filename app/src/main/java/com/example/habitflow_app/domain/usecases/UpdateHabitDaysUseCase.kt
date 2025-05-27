package com.example.habitflow_app.domain.usecases

import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateResponse
import com.example.habitflow_app.features.habits.data.dto.UpdateHabitDaysRequest
import javax.inject.Inject

fun String.normalizeUUID(): String {
    return if (this.length == 32 && !this.contains("-")) {
        "${this.substring(0, 8)}-${this.substring(8, 12)}-${this.substring(12, 16)}-" +
                "${this.substring(16, 20)}-${this.substring(20)}"
    } else {
        this
    }
}

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
            // 1. Obtener hábitos del usuario
            val userHabits = repository.getUserHabits()

            // 2. Buscar el hábito por el ID recibido (puede ser el ID de la vista)
            val habitToUpdate = userHabits.find { it.id == habitId || it.habitId == habitId }
                ?: throw Exception("Habit not found")

            // 3. Usar el habit_id (ID real en Directus)
            val realHabitId = habitToUpdate.habitId

            // 4. Procesar con el ID real
            val normalizedDays = days.map { it.normalizeUUID() }
            val response = repository.updateHabitDays(
                UpdateHabitDaysRequest(
                    habitId = realHabitId.normalizeUUID(), // <-- ID real
                    days = normalizedDays
                )
            )

            if (response.success) Result.success(response)
            else Result.failure(Exception("Failed to update ${days.size - response.updatedCount} days"))
        } catch (e: Exception) {
            Result.failure(Exception("Update failed: ${e.message}"))
        }
    }
}