package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.repositories.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
}