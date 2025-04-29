package com.example.habitflow_app.features.authentication.validation

import javax.inject.Inject

class EmailValidator @Inject constructor() {
    fun validate(email: String): String? {
        return when {
            email.isBlank() -> "El correo electrónico es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Ingresa un correo electrónico válido"
            else -> null
        }
    }
}