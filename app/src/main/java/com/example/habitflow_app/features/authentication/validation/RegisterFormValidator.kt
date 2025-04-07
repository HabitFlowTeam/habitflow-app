package com.example.habitflow_app.features.authentication.validation

import com.example.habitflow_app.core.validation.ValidationResult

/**
 * Coordinates validation for all fields in the registration form
 */
class RegisterFormValidator {
    /**
     * Validates complete registration form data
     *
     * @param fullName User full name
     * @param username User username
     * @param email User email address
     * @param password User password
     * @param confirmPassword Password confirmation
     * @param termsAccepted Whether terms checkbox is checked
     */
    fun validateForm(
        fullName: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        termsAccepted: Boolean
    ): Map<String, ValidationResult> {
        return mapOf(
            "fullName" to AuthValidators.validateFullName(fullName),
            "username" to AuthValidators.validateUsername(username),
            "email" to AuthValidators.validateEmail(email),
            "password" to AuthValidators.validatePassword(password),
            "confirmPassword" to AuthValidators.validatePasswordMatch(password, confirmPassword),
            "terms" to validateTerms(termsAccepted)
        )
    }

    private fun validateTerms(accepted: Boolean): ValidationResult {
        return if (!accepted) {
            ValidationResult(
                isValid = false,
                errorMessage = "Debes aceptar los términos y condiciones"
            )
        } else {
            ValidationResult(isValid = true)
        }
    }
}
