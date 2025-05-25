package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.domain.usecases.UpdateHabitDaysUseCase
import com.example.habitflow_app.features.habits.validation.EditHabitFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitEditViewModel @Inject constructor(
    private val updateHabitDaysUseCase: UpdateHabitDaysUseCase,
    private val habitsRepository: HabitsRepository,
    private val habitValidator: EditHabitFormValidator
) : ViewModel() {

    // Estado inicial
    private val _uiState = MutableStateFlow(HabitEditUiState())
    val uiState: StateFlow<HabitEditUiState> = _uiState

    fun loadInitialData(habitId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val days = habitsRepository.getHabitDays(habitId)
                _uiState.value = _uiState.value.copy(
                    habitId = habitId,
                    selectedDays = days.map { it.weekDayId },
                    isDailySelected = days.size == 7,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al cargar días: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    // Manejador de eventos
    fun onEvent(event: HabitEditEvent) {
        when (event) {
            is HabitEditEvent.DaysChanged -> {
                _uiState.value = _uiState.value.copy(
                    selectedDays = event.value,
                    daysError = null
                )
            }

            is HabitEditEvent.FrequencyChanged -> {
                _uiState.value = _uiState.value.copy(
                    isDailySelected = event.isDaily,
                    selectedDays = if (event.isDaily) emptyList() else _uiState.value.selectedDays,
                    daysError = if (event.isDaily) null else _uiState.value.daysError
                )
            }

            HabitEditEvent.Submit -> {
                validateAndUpdateDays()
            }

            HabitEditEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(
                    error = null
                )
            }
        }
    }

    // Validación y actualización de días
    private fun validateAndUpdateDays() {
        _uiState.value = _uiState.value.copy(
            daysError = null,
            error = null
        )

        val validation = habitValidator.validateDays(
            selectedDays = _uiState.value.selectedDays,
            isDailySelected = _uiState.value.isDailySelected
        )

        if (!validation.isValid) {
            _uiState.value = _uiState.value.copy(
                daysError = validation.errorMessage
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val daysToUpdate = if (_uiState.value.isDailySelected) {
                    getAllWeekDays()
                } else {
                    _uiState.value.selectedDays
                }

                updateHabitDaysUseCase(
                    habitId = _uiState.value.habitId,
                    days = daysToUpdate
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al actualizar días"
                )
            }
        }
    }

    private fun getAllWeekDays(): List<String> = listOf(
        "d3b15c58-711f-40d9-9f4b-06d0d6e925d1", // Lunes
        "b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6", // Martes
        "4afe91c2-9851-4af7-b282-39a543989ea3", // Miércoles
        "ea5e7c7a-182c-4b49-8b2d-2162cd138384", // Jueves
        "22f2bf21-fcbd-473f-98d2-96ba47fabe16", // Viernes
        "f31a5698-2a4d-4818-8a0b-e7f843b9ec14", // Sábado
        "82a4b1c9-72a8-4e91-aaa4-2c92d30b810f"  // Domingo
    )
}

// Estados de la UI para edición
data class HabitEditUiState(
    val habitId: String = "",
    val selectedDays: List<String> = emptyList(),
    val isDailySelected: Boolean = true,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val daysError: String? = null
)

// Eventos para edición
sealed class HabitEditEvent {
    data class DaysChanged(val value: List<String>) : HabitEditEvent()
    data class FrequencyChanged(val isDaily: Boolean) : HabitEditEvent()
    object Submit : HabitEditEvent()
    object ClearError : HabitEditEvent()
}