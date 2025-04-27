package com.example.habitflow_app.features.authentication.validation

import com.example.habitflow_app.core.validation.FormValidator
import com.example.habitflow_app.core.validation.ValidationResult

/** Contains authentication-specific validation rules */
object AuthValidators {
    /**
     * Validates a full name
     *
     * @param fullName The complete name to validate
     * @return ValidationResult with success status and error message if invalid
     */
    fun validateFullName(fullName: String): ValidationResult {
        val basicValidation = FormValidator.validateRequired(fullName, "Nombre completo")
        if (!basicValidation.isValid) return basicValidation

        val lengthValidation =
                FormValidator.validateLength(
                        value = fullName,
                        fieldName = "Nombre completo",
                        minLength = 3,
                        maxLength = 255
                )
        if (!lengthValidation.isValid) return lengthValidation

        return FormValidator.validatePattern(
                value = fullName,
                pattern = Regex("^[\\p{L} .'-]+$"),
                fieldName = "Nombre completo",
                errorMessage = "Solo se permiten letras, espacios y apóstrofes"
        )
    }

    /**
     * Validates a username format
     *
     * @param username The username to validate
     * @return ValidationResult with success status and error message if invalid
     */
    fun validateUsername(username: String): ValidationResult {
        val basicValidation = FormValidator.validateRequired(username, "Nombre de usuario")
        if (!basicValidation.isValid) return basicValidation

        val lengthValidation =
                FormValidator.validateLength(
                        value = username,
                        fieldName = "Nombre de usuario",
                        minLength = 3,
                        maxLength = 255
                )
        if (!lengthValidation.isValid) return lengthValidation

        return FormValidator.validatePattern(
                value = username,
                pattern = Regex("^[a-zA-Z0-9._-]+$"),
                fieldName = "Nombre de usuario",
                errorMessage = "Solo letras, números, puntos, guiones y guiones bajos"
        )
    }

    /**
     * Validates an email address format
     *
     * @param email Email address to validate
     * @return ValidationResult with success status and error message if invalid
     */
    fun validateEmail(email: String): ValidationResult {
        val basicValidation = FormValidator.validateRequired(email, "Correo electrónico")
        if (!basicValidation.isValid) return basicValidation

        return FormValidator.validatePattern(
                value = email,
                pattern = Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"),
                fieldName = "Correo electrónico",
                errorMessage = "Correo electrónico inválido"
        )
    }

    /**
     * Validates password strength requirements
     *
     * @param password Password to validate
     * @return ValidationResult with success status and error message if invalid
     */
    fun validatePassword(password: String): ValidationResult {
        val basicValidation = FormValidator.validateRequired(password, "Contraseña")
        if (!basicValidation.isValid) return basicValidation

        return when {
            password.length < 8 -> ValidationResult(false, "Mínimo 8 caracteres")
            !password.any { it.isDigit() } ->
                    ValidationResult(false, "Debe contener al menos un número")
            !password.any { it.isUpperCase() } ->
                    ValidationResult(false, "Debe contener mayúsculas")
            !password.any { !it.isLetterOrDigit() } ->
                    ValidationResult(false, "Debe incluir un carácter especial (@, !, etc.)")
            else -> ValidationResult(true)
        }
    }

    /**
     * Validates that two password fields match
     *
     * @param password First password entry
     * @param confirmPassword Second password entry
     * @return ValidationResult with success status and error message if invalid
     */
    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        val basicValidation = FormValidator.validateRequired(password, "Confirmar contraseña")
        if (!basicValidation.isValid) return basicValidation

        return if (password != confirmPassword) {
            ValidationResult(isValid = false, errorMessage = "Las contraseñas no coinciden")
        } else {
            ValidationResult(isValid = true)
        }
    }

    /**
     * Validates a password for login
     *
     * @param password Password to validate
     * @return ValidationResult with success status and error message if invalid
     */
    fun validateLoginPassword(password: String): ValidationResult {
        return FormValidator.validateRequired(password, "Contraseña")
    }
}
