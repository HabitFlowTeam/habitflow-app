package com.example.habitflow_app.features.habits.validation

class EditHabitFormValidator {
    fun validateDays(
        selectedDays: List<String>,
        isDailySelected: Boolean
    ): ValidationResult {
        return if (isDailySelected) {
            ValidationResult(isValid = true)
        } else {
            when {
                selectedDays.isEmpty() -> ValidationResult(
                    isValid = false,
                    errorMessage = "Debes seleccionar al menos un día"
                )
                else -> ValidationResult(isValid = true)
            }
        }
    }
}