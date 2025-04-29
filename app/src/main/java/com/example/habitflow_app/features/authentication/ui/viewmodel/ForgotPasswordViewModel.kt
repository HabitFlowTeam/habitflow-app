package com.example.habitflow_app.features.authentication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.features.authentication.validation.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val emailValidator: EmailValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    emailError = null
                )
            }
            ForgotPasswordEvent.Submit -> requestPasswordReset()
        }
    }

    private fun requestPasswordReset() {
        val email = _uiState.value.email
        val emailError = emailValidator.validate(email)

        if (emailError != null) {
            _uiState.value = _uiState.value.copy(
                emailError = emailError,
                isSuccess = false
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                authRepository.requestPasswordReset(email)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    message = "Se ha enviado un enlace para restablecer tu contraseña a tu correo electrónico"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al solicitar el restablecimiento de contraseña"
                )
            }
        }
    }
}

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

sealed class ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent()
    object Submit : ForgotPasswordEvent()
}