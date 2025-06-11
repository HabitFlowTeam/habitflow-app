package com.example.habitflow_app.features.habits.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.domain.usecases.StreakManagementUseCase
import com.example.habitflow_app.features.habits.data.dto.ActiveHabitDto
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ListHabitsViewModel @Inject constructor(
    private val habitsRepository: HabitsRepository,
    private val streakManagementUseCase: StreakManagementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    fun loadHabits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val habits = habitsRepository.getUserHabits()
                val habitUiModels = habits.map { habit ->
                    habit.toUiModel(viewModel = this@ListHabitsViewModel)
                }
                _uiState.value = _uiState.value.copy(
                    habits = habitUiModels,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading habits: ${e.message}"
                )
            }
        }
    }

    fun updateHabitStatus(habitId: String, isChecked: Boolean) {
        Log.d("HabitsViewModel", "updateHabitStatus called: habitId=$habitId, isChecked=$isChecked")

        val currentHabit = _uiState.value.habits.find { it.id == habitId }
        if (currentHabit == null) {
            Log.e("HabitsViewModel", "Habit not found: $habitId")
            return
        }

        // OPTIMISTIC UI UPDATE (immediate)
        val optimisticStreak = if (isChecked) {
            // If checked: increment streak
            currentHabit.streak + 1
        } else {
            // If unchecked: streak should be current -1, but minimum 0
            // CORRECTION: If I have a streak of 13 and unchecked today, it should become 12
            maxOf(0, currentHabit.streak - 1)
        }

        // Update UI immediately with optimistic values
        updateUIState(habitId, isChecked, optimisticStreak)

        Log.d(
            "HabitsViewModel",
            "Optimistic UI update - isChecked: $isChecked, currentStreak: ${currentHabit.streak}, optimisticStreak: $optimisticStreak"
        )

        // BACKGROUND OPERATION (verification and correction if needed)
        viewModelScope.launch {
            try {
                val habitTrackingId = currentHabit.habitTrackingId

                // 1. Update habit tracking in database
                val habitTracking = if (habitTrackingId != null) {
                    habitsRepository.updateHabitTrackingCheck(habitTrackingId, isChecked)
                } else {
                    habitsRepository.createHabitTracking(habitId, isChecked)
                }

                Log.d("HabitsViewModel", "Database tracking updated: ${habitTracking.isChecked}")

                // 2. Manage streak in database
                val actualStreak = try {
                    val streakResult = streakManagementUseCase(habitId, isChecked)
                    streakResult.fold(
                        onSuccess = { calculatedStreak ->
                            Log.d(
                                "HabitsViewModel",
                                "Streak calculated successfully: $calculatedStreak"
                            )
                            calculatedStreak
                        },
                        onFailure = { error ->
                            Log.e("HabitsViewModel", "Error calculating streak: ${error.message}")
                            // If calculation fails, use optimistic prediction as fallback
                            optimisticStreak
                        }
                    )
                } catch (e: Exception) {
                    Log.e("HabitsViewModel", "Exception in streak management: ${e.message}")
                    optimisticStreak
                }

                // 3. CORRECTION: Only update if there's significant difference from optimistic prediction
                if (actualStreak != optimisticStreak) {
                    Log.d(
                        "HabitsViewModel",
                        "Correcting streak: optimistic=$optimisticStreak, actual=$actualStreak"
                    )
                    updateUIState(habitId, habitTracking.isChecked, actualStreak, habitTracking.id)
                } else {
                    // Only update habitTrackingId if different
                    if (habitTracking.id != currentHabit.habitTrackingId) {
                        updateUIState(
                            habitId,
                            habitTracking.isChecked,
                            actualStreak,
                            habitTracking.id
                        )
                    }
                    Log.d("HabitsViewModel", "Optimistic prediction was correct: $actualStreak")
                }

            } catch (e: Exception) {
                Log.e("HabitsViewModel", "Error in background operation: ${e.message}", e)

                // In case of error, revert to original state
                updateUIState(
                    habitId,
                    currentHabit.isChecked,
                    currentHabit.streak,
                    currentHabit.habitTrackingId
                )

                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error updating habit: ${e.message}"
                )
            }
        }
    }

    /**
     * Helper function to update UI state consistently
     */
    private fun updateUIState(
        habitId: String,
        isChecked: Boolean,
        newStreak: Int? = null,
        newHabitTrackingId: String? = null
    ) {
        val currentHabits = _uiState.value.habits
        val updatedHabits = currentHabits.map { habit ->
            if (habit.id == habitId) {
                habit.copy(
                    isChecked = isChecked,
                    streak = newStreak
                        ?: habit.streak, // Only update if new value is provided
                    habitTrackingId = newHabitTrackingId ?: habit.habitTrackingId
                )
            } else {
                habit
            }
        }

        _uiState.value = _uiState.value.copy(habits = updatedHabits)

        Log.d(
            "HabitsViewModel",
            "UI State updated for habit $habitId - isChecked: $isChecked, streak: ${newStreak ?: "unchanged"}"
        )
    }

    private fun formatScheduledDays(scheduledDays: List<String>): String {
        return when {
            scheduledDays.size == 7 -> "Todos los días"
            scheduledDays.isEmpty() -> "No hay días programados"
            else -> scheduledDays.joinToString(", ")
        }
    }

    private fun ActiveHabitDto.toUiModel(
        viewModel: ListHabitsViewModel
    ): HabitUiModel {
        return HabitUiModel(
            id = this.id,
            name = this.name,
            days = formatScheduledDays(this.scheduledDays),
            streak = this.streak,
            isChecked = this.isCheckedToday,
            habitTrackingId = this.habitTrackingId,
            onCheckedChange = { isChecked ->
                viewModel.updateHabitStatus(this.id, isChecked)
            }
        )
    }
}

data class HabitsUiState(
    val habits: List<HabitUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class HabitUiModel(
    val id: String,
    val name: String,
    val days: String, // Days formatted as string for display in UI
    val habitTrackingId: String?,
    val streak: Int,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit = {}
)