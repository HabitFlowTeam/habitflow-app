package com.example.habitflow_app.features.authentication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.exceptions.*
import com.example.habitflow_app.domain.usecases.LoginUserUseCase
import com.example.habitflow_app.features.authentication.validation.LoginFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling user login logic.
 * Manages form state, validation, and login process.
 *
 * @param loginUserUseCase Use case for user login
 * @param formValidator Validator for login form fields
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val formValidator: LoginFormValidator
) : ViewModel() {

    // Mutable state holder
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Handles login events from the UI.
     *
     * @param event The triggered login event
     */
    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    emailError = null
                )
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = event.value,
                    passwordError = null
                )
            }

            LoginEvent.Submit -> validateAndLogin()
        }
    }

    /**
     * Validates form fields and initiates login if valid.
     */
    private fun validateAndLogin() {
        // Clear previous errors
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null,
            error = null
        )

        // Validate all form fields
        val validationResults = formValidator.validateForm(
            email = _uiState.value.email,
            password = _uiState.value.password
        )

        val hasErrors = validationResults.any { !it.value.isValid }

        // Update state with validation errors
        _uiState.value = _uiState.value.copy(
            emailError = validationResults["email"]?.errorMessage,
            passwordError = validationResults["password"]?.errorMessage,
            isLoading = !hasErrors
        )

        // Proceed with login if no errors
        if (!hasErrors) {
            viewModelScope.launch {
                try {
                    loginUserUseCase(
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    // Handle specific errors
                    when (e) {
                        is InvalidCredentialsException -> {
                            _uiState.value = _uiState.value.copy(
                                error = e.message,
                                isLoading = false
                            )
                        }

                        is NetworkConnectionException -> {
                            _uiState.value = _uiState.value.copy(
                                error = e.message,
                                isLoading = false
                            )
                        }

                        is SessionExpiredException -> {
                            _uiState.value = _uiState.value.copy(
                                error = e.message,
                                isLoading = false
                            )
                        }

                        else -> {
                            _uiState.value = _uiState.value.copy(
                                error = e.message ?: "Error desconocido al iniciar sesión",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * UI state representation for login screen.
 *
 * @property email User's email input
 * @property password User's password input
 * @property emailError Email validation error
 * @property passwordError Password validation error
 * @property isLoading Login in progress flag
 * @property isSuccess Login success flag
 * @property error General login error message
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

/**
 * Sealed class representing all possible login events.
 */
sealed class LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data object Submit : LoginEvent()
}