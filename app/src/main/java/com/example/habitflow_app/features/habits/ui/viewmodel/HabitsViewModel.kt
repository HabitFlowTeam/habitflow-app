package com.example.habitflow_app.features.habits.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.dto.HabitDayCreateRequest
import com.example.habitflow_app.features.habits.data.dto.HabitDayUpdateRequest
import com.example.habitflow_app.features.habits.data.dto.HabitRequest
import com.example.habitflow_app.features.habits.data.dto.HabitUpdateRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val habitsRepository: HabitsRepository,
    private val extractInfoToken: ExtractInfoToken
): ViewModel() {
    private val _habitsState = MutableStateFlow<List<Habit>>(emptyList())
    val habitsState: StateFlow<List<Habit>> = _habitsState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun loadHabits(){
        viewModelScope.launch {
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                if (accessToken.isNullOrBlank()) {
                    _error.value = "Autenticación requerida"
                    return@launch
                }

                val userId = extractInfoToken.extractUserIdFromToken(accessToken)
                if (userId.isNullOrBlank()) {
                    _error.value = "ID de usuario inválido"
                    return@launch
                }

                val habits = habitsRepository.getHabits(userId)
                _habitsState.value = habits

            }catch (e: Exception){

            }
        }
    }

    fun createHabit(
        name: String,
        categoryId: String,
        selectedDays: List<String>,
        reminderTime: LocalTime? = null
    ) {
        viewModelScope.launch {
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                if (accessToken.isNullOrBlank()) {
                    _error.value = "Autenticación requerida"
                    return@launch
                }

                val userId = extractInfoToken.extractUserIdFromToken(accessToken)
                if (userId.isNullOrBlank()) {
                    _error.value = "ID de usuario inválido"
                    return@launch
                }

                val request = HabitRequest(
                    name = name,
                    userId = userId,
                    categoryId = categoryId,
                    reminderTime = reminderTime?.toString(),
                    days = selectedDays.map { HabitDayCreateRequest(weekDayId = it) }
                )

                val newHabit = habitsRepository.createHabit(request)
                _habitsState.value = _habitsState.value + newHabit // Actualiza el estado

            } catch (e: Exception) {
                _error.value = "Error al crear hábito: ${e.message}"
                Log.e("HabitsVM", "Error en createHabit", e)
            }
        }
    }

    fun updateHabit(
        habitId: String,
        name: String? = null,
        categoryId: String? = null,
        selectedDays: List<Pair<String?, String>>? = null, // Pair(idExistente?, weekDayId)
        reminderTime: LocalTime? = null,
        notificationsEnabled: Boolean? = null
    ) {
        viewModelScope.launch {
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                if (accessToken.isNullOrBlank()) {
                    _error.value = "Autenticación requerida"
                    return@launch
                }

                val request = HabitUpdateRequest(
                    name = name,
                    categoryId = categoryId,
                    reminderTime = reminderTime?.toString(),
                    notificationsEnabled = notificationsEnabled,
                    days = selectedDays?.map {
                        HabitDayUpdateRequest(id = it.first, weekDayId = it.second)
                    }
                )

                val updatedHabit = habitsRepository.updateHabit(habitId, request)
                // update the list
                _habitsState.value = _habitsState.value.map {
                    if (it.id == habitId) updatedHabit else it
                }

            } catch (e: Exception) {
                _error.value = "Error al actualizar hábito: ${e.message}"
                Log.e("HabitsVM", "updateHabit error", e)
            }
        }
    }

    fun softDeleteHabit(habitId: String) {
        viewModelScope.launch {
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                if (accessToken.isNullOrBlank()) {
                    _error.value = "Autenticación requerida"
                    return@launch
                }

                val success = habitsRepository.softDeleteHabit(habitId)
                if (success) {
                    _habitsState.value = _habitsState.value.map {
                        if (it.id == habitId) it.copy(isDeleted = true) else it
                    }
                } else {
                    _error.value = "No se pudo eliminar el hábito"
                }
            } catch (e: Exception) {
                _error.value = "Error al eliminar hábito: ${e.message}"
                Log.e("HabitsVM", "softDeleteHabit error", e)
            }
        }
    }

}