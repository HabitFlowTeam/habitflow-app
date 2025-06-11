package com.example.habitflow_app.domain.usecases

import android.util.Log
import com.example.habitflow_app.domain.repositories.HabitsRepository
import javax.inject.Inject

/**
 * Fixed version of streak management that properly handles increment/decrement.
 * This version correctly decreases streak when habits are unchecked.
 */
class StreakManagementUseCase @Inject constructor(
    private val habitsRepository: HabitsRepository
) {
    private companion object {
        const val TAG = "StreakManagement"
    }

    /**
     * Enhanced streak update: increment if checked, decrement if unchecked.
     *
     * @param habitId The ID of the habit to update
     * @param isCompleted Whether the habit was completed today
     * @return Result containing the new streak value
     */
    suspend operator fun invoke(
        habitId: String,
        isCompleted: Boolean
    ): Result<Int> {
        return try {
            Log.d(TAG, "Managing streak for habit $habitId, completed: $isCompleted")

            // Get current streak
            val currentStreak = habitsRepository.getCurrentHabitStreak(habitId)
            Log.d(TAG, "Current streak: $currentStreak")

            val newStreak = if (isCompleted) {
                // If completed: increment streak
                currentStreak + 1
            } else {
                // If unchecked: decrement streak (minimum 0)
                // FIXED: This was the missing logic!
                maxOf(0, currentStreak - 1)
            }

            Log.d(TAG, "Calculated new streak: $currentStreak -> $newStreak")

            // Update the streak in database
            val success = habitsRepository.updateHabitStreak(habitId, newStreak)

            if (success) {
                Log.d(
                    TAG,
                    "Successfully updated streak for habit $habitId from $currentStreak to $newStreak"
                )
                Result.success(newStreak)
            } else {
                Log.e(TAG, "Failed to update streak for habit $habitId")
                Result.failure(Exception("Failed to update habit streak"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in streak management", e)
            // Return current streak as fallback
            try {
                val currentStreak = habitsRepository.getCurrentHabitStreak(habitId)
                Log.w(TAG, "Using fallback streak: $currentStreak")
                Result.success(currentStreak)
            } catch (fallbackError: Exception) {
                Log.e(TAG, "Even fallback failed", fallbackError)
                Result.failure(e)
            }
        }
    }
}