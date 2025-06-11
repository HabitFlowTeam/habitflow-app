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
                    errorMessage = "Error al cargar los hábitos: ${e.message}"
                )
            }
        }
    }

    fun updateHabitStatus(habitId: String, isChecked: Boolean) {
        Log.d("HabitsViewModel", "updateHabitStatus called: habitId=$habitId, isChecked=$isChecked")

        viewModelScope.launch {
            try {
                val currentHabit = _uiState.value.habits.find { it.id == habitId }
                Log.d(
                    "HabitsViewModel",
                    "Current habit found: ${currentHabit?.name}, current isChecked: ${currentHabit?.isChecked}"
                )

                val habitTrackingId = currentHabit?.habitTrackingId

                // Update habit tracking
                val habitTracking = if (habitTrackingId != null) {
                    habitsRepository.updateHabitTrackingCheck(habitTrackingId, isChecked)
                } else {
                    habitsRepository.createHabitTracking(habitId, isChecked)
                }

                Log.d(
                    "HabitsViewModel",
                    "Database updated successfully: ${habitTracking.isChecked}"
                )

                // Simple streak management: only calculate when user interacts
                val newStreak = try {
                    val streakResult = streakManagementUseCase(habitId, isChecked)
                    streakResult.getOrElse {
                        Log.e("HabitsViewModel", "Error managing streak: ${it.message}")
                        currentHabit?.streak ?: 0
                    }
                } catch (e: Exception) {
                    Log.e("HabitsViewModel", "Exception managing streak: ${e.message}")
                    currentHabit?.streak ?: 0
                }

                // Update UI state with new tracking status and streak
                val currentHabits = _uiState.value.habits
                val updatedHabits = currentHabits.map { habit ->
                    if (habit.id == habitId) {
                        habit.copy(
                            habitTrackingId = habitTracking.id,
                            isChecked = habitTracking.isChecked,
                            streak = newStreak
                        )
                    } else {
                        habit
                    }
                }

                _uiState.value = _uiState.value.copy(habits = updatedHabits)
                Log.d(
                    "HabitsViewModel",
                    "UI State updated, new habits count: ${updatedHabits.size}, new streak: $newStreak"
                )

            } catch (e: Exception) {
                Log.e("HabitsViewModel", "Error updating habit: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error updating habit: ${e.message}"
                )
            }
        }
    }

    private fun formatScheduledDays(scheduledDays: List<String>): String {
        return when {
            scheduledDays.size == 7 -> "Todos los días"
            scheduledDays.isEmpty() -> "Sin días programados"
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