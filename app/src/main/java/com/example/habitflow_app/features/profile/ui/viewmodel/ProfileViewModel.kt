package com.example.habitflow_app.features.profile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.repositories.ProfileRepository
import com.example.habitflow_app.domain.repositories.HabitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val extractInfoToken: ExtractInfoToken,
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<Profile?>(null)
    val profileState: StateFlow<Profile?> get() = _profileState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _completedHabitsCount = MutableStateFlow(0)
    val completedHabitsCount: StateFlow<Int> get() = _completedHabitsCount

    fun loadProfile() {
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

                val profile = profileRepository.getProfile(userId)
                _profileState.value = profile
                // Obtener hábitos completados
                _completedHabitsCount.value = habitsRepository.getCompletedHabitsCount(userId)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
            } catch (e: Exception) {
                _error.value = "Error al cerrar sesión: ${e.message}"
            }
        }
    }

}
