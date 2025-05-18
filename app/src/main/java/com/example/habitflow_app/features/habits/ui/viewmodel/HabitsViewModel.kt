package com.example.habitflow_app.features.habits.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.repositories.HabitsRepository
import com.example.habitflow_app.features.habits.data.dto.HabitDayCreateRequest
import com.example.habitflow_app.features.habits.data.dto.HabitRequest
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

}