package com.example.habitflow_app.domain.usecases

import android.util.Log
import com.example.habitflow_app.domain.repositories.HabitsRepository
import javax.inject.Inject

/**
 * Simplified version of streak management that doesn't require complex API calls.
 * This version only handles basic streak increment/decrement based on user actions.
 */
class StreakManagementUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    private companion object {
        const val TAG = "SimpleStreakManagement"
    }

    /**
     * Simple streak update: increment if checked, keep if unchecked for today.
     * More complex logic can be added later when API issues are resolved.
     */
    suspend operator fun invoke(
        habitId: String,
        isCompleted: Boolean
    ): Result<Int> {
        return try {
            Log.d(TAG, "Simple streak management for habit $habitId, completed: $isCompleted")

            // Get current streak
            val currentStreak = habitsRepository.getCurrentHabitStreak(habitId)

            val newStreak = if (isCompleted) {
                // If completed, increment streak
                currentStreak + 1
            } else {
                // If unchecked, we could either:
                // 1. Keep current streak (user might complete later today)
                // 2. Reset to 0 (strict mode)
                // For now, let's keep it simple and just maintain current streak
                currentStreak
            }

            // Update the streak
            val success = habitsRepository.updateHabitStreak(habitId, newStreak)

            if (success) {
                Log.d(TAG, "Updated streak for habit $habitId from $currentStreak to $newStreak")
                Result.success(newStreak)
            } else {
                Log.e(TAG, "Failed to update streak for habit $habitId")
                Result.failure(Exception("Failed to update habit streak"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in simple streak management", e)
            // Return current streak as fallback
            try {
                val currentStreak = habitsRepository.getCurrentHabitStreak(habitId)
                Result.success(currentStreak)
            } catch (fallbackError: Exception) {
                Log.e(TAG, "Even fallback failed", fallbackError)
                Result.failure(e)
            }
        }
    }

}