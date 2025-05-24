package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.repositories.HabitsRepository
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
    private val habitsRepository: HabitsRepository,
    private val habitValidator: HabitFormValidator
) : ViewModel() {


    // Estado inicial
    private val _uiState = MutableStateFlow(HabitCreationUiState())
    val uiState: StateFlow<HabitCreationUiState> = _uiState

    init {
        loadCategories()
    }

    // Cargar categorías al inicializar
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingCategories = true,
                categoriesError = null
            )

            try {
                val categories = habitsRepository.getCategories() // Llamada directa al repositorio
                _uiState.value = _uiState.value.copy(
                    categories = categories,
                    isLoadingCategories = false,
                    categoryId = categories.firstOrNull()?.id ?: "" // Auto-selección primera categoría
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    categoriesError = e.message ?: "Error al cargar categorías",
                    isLoadingCategories = false
                )
            }
        }
    }

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

            is HabitCreationEvent.FrequencyChanged -> {
                _uiState.value = _uiState.value.copy(
                    isDailySelected = event.isDaily,
                    selectedDays = if (event.isDaily) emptyList() else _uiState.value.selectedDays,
                    daysError = if (event.isDaily) null else _uiState.value.daysError
                )
            }

            HabitCreationEvent.Submit -> {
                validateAndCreateHabit()
            }

            HabitCreationEvent.RetryLoadCategories -> {
                loadCategories()
            }

            HabitCreationEvent.ClearError -> {
                _uiState.value = _uiState.value.copy(
                    error = null,
                    categoriesError = null
                )
            }
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
            isDailySelected = _uiState.value.isDailySelected,
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
                    val daysToSend = if (_uiState.value.isDailySelected) {
                        // Todos los días si es diario
                        listOf(
                            "d3b15c58-711f-40d9-9f4b-06d0d6e925d1", // Lunes
                            "b9b3995e-c6a5-46c7-bf8a-f1c1c2e65dd6", // Martes
                            "4afe91c2-9851-4af7-b282-39a543989ea3", // Miércoles
                            "ea5e7c7a-182c-4b49-8b2d-2162cd138384", // Jueves
                            "22f2bf21-fcbd-473f-98d2-96ba47fabe16", // Viernes
                            "f31a5698-2a4d-4818-8a0b-e7f843b9ec14", // Sábado
                            "82a4b1c9-72a8-4e91-aaa4-2c92d30b810f"  // Domingo
                        )
                    } else {
                        _uiState.value.selectedDays
                    }

                    val response = createHabitUseCase(
                        CreateHabitRequest(
                            name = _uiState.value.name,
                            categoryId = _uiState.value.categoryId,
                            selectedDays = daysToSend,
                            notificationsEnabled = _uiState.value.notificationsEnabled,
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
    val categories: List<Category> = emptyList(),
    val isLoadingCategories: Boolean = false,
    val categoriesError: String? = null,
    val nameError: String? = null,
    val categoryError: String? = null,
    val daysError: String? = null,
    val reminderError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val createdHabit: HabitResponse? = null,
    val isDailySelected: Boolean = true,
)

// Eventos
sealed class HabitCreationEvent {
    data class NameChanged(val value: String) : HabitCreationEvent()
    data class CategoryChanged(val value: String) : HabitCreationEvent()
    data class DaysChanged(val value: List<String>) : HabitCreationEvent()
    data class ReminderTimeChanged(val value: LocalTime?) : HabitCreationEvent()
    data class NotificationsToggled(val isEnabled: Boolean) : HabitCreationEvent()
    data class FrequencyChanged(val isDaily: Boolean) : HabitCreationEvent()
    object Submit : HabitCreationEvent()
    object RetryLoadCategories : HabitCreationEvent()
    object ClearError : HabitCreationEvent()
}

// Modelo de respuesta
data class HabitResponse(
    val name: String,
    val categoryId: String,
    val selectedDays: List<String>,
    val reminderTime: LocalTime?
)