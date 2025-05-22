package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.usecases.CreateHabitUseCase
import com.example.habitflow_app.features.habits.data.dto.CreateHabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitResponse
import com.example.habitflow_app.features.habits.validation.HabitFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val createHabitUseCase: CreateHabitUseCase,
    private val habitValidator: HabitFormValidator
) : ViewModel() {

    // Estado inicial
    private val _uiState = MutableStateFlow(HabitCreationUiState())
    val uiState: StateFlow<HabitCreationUiState> = _uiState

    // Manejador de eventos
    fun onEvent(event: HabitCreationEvent) {
        when (event) {
            is HabitCreationEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(
                    name = event.value,
                    nameError = null
                )
            }

            is HabitCreationEvent.CategoryChanged -> {
                _uiState.value = _uiState.value.copy(
                    categoryId = event.value,
                    categoryError = null
                )
            }

            is HabitCreationEvent.DaysChanged -> {
                _uiState.value = _uiState.value.copy(
                    selectedDays = event.value,
                    daysError = null
                )
            }

            is HabitCreationEvent.ReminderTimeChanged -> {
                _uiState.value = _uiState.value.copy(
                    reminderTime = event.value,
                    reminderError = null
                )
            }

            is HabitCreationEvent.NotificationsToggled -> {
                _uiState.value = _uiState.value.copy(
                    notificationsEnabled = event.isEnabled,
                    reminderError = if (!event.isEnabled) null else _uiState.value.reminderError
                )
            }

            HabitCreationEvent.Submit -> validateAndCreateHabit()
        }
    }

    // Validación y creación del hábito
    private fun validateAndCreateHabit() {
        // Resetear errores previos
        _uiState.value = _uiState.value.copy(
            nameError = null,
            categoryError = null,
            daysError = null,
            reminderError = null,
            error = null
        )

        // Validar campos
        val validationResults = habitValidator.validateForm(
            name = _uiState.value.name,
            categoryId = _uiState.value.categoryId,
            selectedDays = _uiState.value.selectedDays,
        )

        // Verificar si hay errores
        val hasErrors = validationResults.any { !it.value.isValid }

        // Actualizar estado con errores
        _uiState.value = _uiState.value.copy(
            nameError = validationResults["name"]?.errorMessage,
            categoryError = validationResults["category"]?.errorMessage,
            daysError = validationResults["days"]?.errorMessage,
            reminderError = validationResults["reminder"]?.errorMessage,
            isLoading = !hasErrors
        )

        // Si no hay errores, proceder con la creación
        if (!hasErrors) {
            viewModelScope.launch {
                try {
                    val response = createHabitUseCase(
                        CreateHabitRequest(
                            name = _uiState.value.name,
                            categoryId = _uiState.value.categoryId,
                            selectedDays = _uiState.value.selectedDays,
                            reminderTime = if (_uiState.value.notificationsEnabled) _uiState.value.reminderTime else null
                        )
                    )

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        createdHabit = response
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Error desconocido al crear el hábito"
                    )
                }
            }
        }
    }
}

// Estados de la UI
data class HabitCreationUiState(
    val name: String = "",
    val categoryId: String = "",
    val selectedDays: List<String> = emptyList(),
    val reminderTime: LocalTime? = null,
    val notificationsEnabled: Boolean = false,
    val nameError: String? = null,
    val categoryError: String? = null,
    val daysError: String? = null,
    val reminderError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val createdHabit: HabitResponse? = null
)

// Eventos
sealed class HabitCreationEvent {
    data class NameChanged(val value: String) : HabitCreationEvent()
    data class CategoryChanged(val value: String) : HabitCreationEvent()
    data class DaysChanged(val value: List<String>) : HabitCreationEvent()
    data class ReminderTimeChanged(val value: LocalTime?) : HabitCreationEvent()
    data class NotificationsToggled(val isEnabled: Boolean) : HabitCreationEvent()
    object Submit : HabitCreationEvent()
}

// Modelo de respuesta
data class HabitResponse(
    val name: String,
    val categoryId: String,
    val selectedDays: List<String>,
    val reminderTime: LocalTime?
)