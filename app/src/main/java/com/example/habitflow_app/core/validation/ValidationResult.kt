package com.example.habitflow_app.core.validation

/**
 * Represents the result of a validation operation
 *
 * @property isValid Indicates if the validation passed successfully
 * @property errorMessage Descriptive message when validation fails (empty if valid)
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)
