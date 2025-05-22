package com.example.habitflow_app.features.habits.validation

class HabitFormValidator {
    fun validateForm(
        name: String,
        categoryId: String,
        selectedDays: List<String>
    ): Map<String, ValidationResult> {
        val results = mutableMapOf<String, ValidationResult>()

        // Validar nombre
        when {
            name.isBlank() -> results["name"] = ValidationResult(
                isValid = false,
                errorMessage = "El nombre no puede estar vacío"
            )
            name.length > 100 -> results["name"] = ValidationResult(
                isValid = false,
                errorMessage = "El nombre es demasiado largo"
            )
            else -> results["name"] = ValidationResult(isValid = true)
        }

        // Validar categoría
        if (categoryId.isBlank()) {
            results["category"] = ValidationResult(
                isValid = false,
                errorMessage = "Selecciona una categoría"
            )
        } else {
            results["category"] = ValidationResult(isValid = true)
        }

        // Validar días seleccionados
        if (selectedDays.isEmpty()) {
            results["days"] = ValidationResult(
                isValid = false,
                errorMessage = "Selecciona al menos un día"
            )
        } else {
            results["days"] = ValidationResult(isValid = true)
        }

        return results
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)