package com.example.habitflow_app.features.authentication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.domain.usecases.RegisterUserUseCase
import com.example.habitflow_app.features.authentication.validation.RegisterFormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling user registration logic.
 * Manages form state, validation, and registration process.
 *
 * @param registerUserUseCase Use case for user registration
 * @param formValidator Validator for registration form fields
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val formValidator: RegisterFormValidator
) : ViewModel() {

    // Mutable state holder
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    /**
     * Handles registration events from the UI.
     *
     * @param event The triggered registration event
     */
    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.FullNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    fullName = event.value,
                    fullNameError = null
                )
            }

            is RegisterEvent.UsernameChanged -> {
                _uiState.value = _uiState.value.copy(
                    username = event.value,
                    usernameError = null
                )
            }

            is RegisterEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    emailError = null
                )
            }

            is RegisterEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = event.value,
                    passwordError = null
                )
            }

            is RegisterEvent.ConfirmPasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    confirmPassword = event.value,
                    confirmPasswordError = null
                )
            }

            is RegisterEvent.TermsAcceptedChanged -> {
                _uiState.value = _uiState.value.copy(
                    termsAccepted = event.value,
                    termsError = null
                )
            }

            RegisterEvent.Submit -> validateAndRegister()
        }
    }

    /**
     * Validates form fields and initiates registration if valid.
     */
    private fun validateAndRegister() {
        // Validate all form fields
        val validationResults = formValidator.validateForm(
            fullName = _uiState.value.fullName,
            username = _uiState.value.username,
            email = _uiState.value.email,
            password = _uiState.value.password,
            confirmPassword = _uiState.value.confirmPassword,
            termsAccepted = _uiState.value.termsAccepted
        )

        val hasErrors = validationResults.any { !it.value.isValid }

        // Update state with validation errors
        _uiState.value = _uiState.value.copy(
            fullNameError = validationResults["fullName"]?.errorMessage,
            usernameError = validationResults["username"]?.errorMessage,
            emailError = validationResults["email"]?.errorMessage,
            passwordError = validationResults["password"]?.errorMessage,
            confirmPasswordError = validationResults["confirmPassword"]?.errorMessage,
            termsError = validationResults["terms"]?.errorMessage,
            isLoading = !hasErrors
        )

        // Proceed with registration if no errors
        if (!hasErrors) {
            viewModelScope.launch {
                try {
                    val user = User(
                        id = "",
                        fullName = _uiState.value.fullName,
                        username = _uiState.value.username,
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )

                    registerUserUseCase(user)
                    _uiState.value = _uiState.value.copy(
                        isSuccess = true,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Error al registrar el usuario",
                        isLoading = false
                    )
                }
            }
        }
    }
}

/**
 * UI state representation for registration screen.
 *
 * @property fullName User's full name input
 * @property username User's username input
 * @property email User's email input
 * @property password User's password input
 * @property confirmPassword Password confirmation input
 * @property termsAccepted Terms acceptance status
 * @property fullNameError Full name validation error
 * @property usernameError Username validation error
 * @property emailError Email validation error
 * @property passwordError Password validation error
 * @property confirmPasswordError Password confirmation error
 * @property termsError Terms acceptance error
 * @property isLoading Registration in progress flag
 * @property isSuccess Registration success flag
 * @property error General registration error message
 */
data class RegisterUiState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAccepted: Boolean = false,
    val fullNameError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

/**
 * Sealed class representing all possible registration events.
 */
sealed class RegisterEvent {
    data class FullNameChanged(val value: String) : RegisterEvent()
    data class UsernameChanged(val value: String) : RegisterEvent()
    data class EmailChanged(val value: String) : RegisterEvent()
    data class PasswordChanged(val value: String) : RegisterEvent()
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent()
    data class TermsAcceptedChanged(val value: Boolean) : RegisterEvent()
    data object Submit : RegisterEvent()
}
