package com.example.habitflow_app.features.authentication.validation

import com.example.habitflow_app.core.validation.ValidationResult

/** Coordinates validation for all fields in the login form */
class LoginFormValidator {
    /**
     * Validates login form data
     *
     * @param email User email address
     * @param password User password
     */
    fun validateForm(email: String, password: String): Map<String, ValidationResult> {
        return mapOf(
                "email" to AuthValidators.validateEmail(email),
                "password" to AuthValidators.validateLoginPassword(password)
        )
    }
}
