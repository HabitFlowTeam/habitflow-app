package com.example.habitflow_app.core.validation

/**
 * Utility class for global form field validations that can be used across the app.
 */
object FormValidator {
    /**
     * Validates that a field is not empty.
     *
     * @param value The value to validate
     * @param fieldName The name of the field for the error message
     * @return ValidationResult indicating if the field is valid
     */
    fun validateRequired(value: String, fieldName: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult(
                isValid = false,
                errorMessage = "$fieldName es obligatorio"
            )
        } else {
            ValidationResult(isValid = true)
        }
    }

    /**
     * Validates minimum and maximum length constraints in a single operation
     *
     * @param value The input string to validate
     * @param fieldName Display name of the field
     * @param minLength Minimum allowed characters (null for no minimum)
     * @param maxLength Maximum allowed characters (null for no maximum)
     * @return ValidationResult indicating if the field is valid
     */
    fun validateLength(
        value: String,
        fieldName: String,
        minLength: Int? = null,
        maxLength: Int? = null
    ): ValidationResult {
        return when {
            minLength != null && value.length < minLength ->
                ValidationResult(
                    isValid = false,
                    errorMessage = "$fieldName debe tener al menos $minLength caracteres"
                )

            maxLength != null && value.length > maxLength ->
                ValidationResult(
                    isValid = false,
                    errorMessage = "$fieldName no debe exceder $maxLength caracteres"
                )

            else -> ValidationResult(isValid = true)
        }
    }

    /**
     * Validates that a field matches a specific pattern.
     *
     * @param value The value to validate
     * @param pattern The regex pattern to match
     * @param fieldName The name of the field for the error message
     * @param errorMessage Custom error message
     * @return ValidationResult indicating if the field is valid
     */
    fun validatePattern(
        value: String,
        pattern: Regex,
        fieldName: String,
        errorMessage: String? = null
    ): ValidationResult {
        return if (!pattern.matches(value)) {
            ValidationResult(
                isValid = false,
                errorMessage = errorMessage ?: "$fieldName tiene un formato inválido"
            )
        } else {
            ValidationResult(isValid = true)
        }
    }
}
