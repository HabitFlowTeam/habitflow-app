package com.example.habitflow_app.core.exceptions

import org.json.JSONObject

/**
 * Base exception class for authentication related errors.
 */
sealed class AuthException(message: String) : Exception(message)

/**
 * Exception thrown when attempting to register with an email that is already in use.
 */
class EmailAlreadyExistsException : AuthException("El correo electrónico ya está registrado")

/**
 * Exception thrown when attempting to register with a username that is already in use.
 */
class UsernameAlreadyExistsException : AuthException("El nombre de usuario ya está en uso")

/**
 * Exception thrown when there's a network connectivity issue.
 */
class NetworkConnectionException : AuthException("Error de conexión, verifica tu red")

/**
 * Exception thrown when the provided credentials are invalid.
 */
class InvalidCredentialsException :
    AuthException("Credenciales incorrectas. Verifica tu email y contraseña")

/**
 * Exception thrown when the user's session has expired.
 */
class SessionExpiredException : AuthException("Tu sesión ha expirado. Inicia sesión nuevamente")

/**
 * Exception thrown when the API returns an unexpected error.
 */
class ApiException(message: String) : AuthException(message)

/**
 * Exception thrown when user doesn't have sufficient permissions to perform an action.
 */
class InsufficientPermissionsException :
    AuthException("No tienes permisos para realizar esta acción")

/**
 * Helper object to handle and parse API errors.
 */
object AuthExceptionHandler {

    /**
     * Parse error responses from Directus API and convert them to specific exceptions.
     *
     * @param errorBody The error response body as string
     * @param httpCode Optional HTTP code for additional context
     * @return An appropriate AuthException
     */
    fun parseDirectusError(errorBody: String, httpCode: Int? = null): AuthException {
        return try {
            // Special case for common HTTP codes
            if (httpCode != null) {
                when (httpCode) {
                    401 -> return handleAuth401Error(errorBody)
                    403 -> return InsufficientPermissionsException()
                    404 -> return ApiException("Recurso no encontrado")
                    429 -> return ApiException("Demasiadas solicitudes. Intenta de nuevo más tarde")
                }
            }

            val jsonObject = JSONObject(errorBody)
            val errorsArray = jsonObject.optJSONArray("errors")

            if (errorsArray != null && errorsArray.length() > 0) {
                val errorObj = errorsArray.getJSONObject(0)
                val message = errorObj.optString("message", "")
                val extensions = errorObj.optJSONObject("extensions")

                // Handle specific error messages
                when {
                    message.contains("credentials", ignoreCase = true) -> {
                        return InvalidCredentialsException()
                    }
                }

                // Process extensions for field-specific errors
                if (extensions != null) {
                    val field = extensions.optString("field", "")
                    val code = extensions.optString("code", "")

                    // Check for unique constraint violations
                    if (code == "RECORD_NOT_UNIQUE") {
                        when (field.lowercase()) {
                            "email" -> return EmailAlreadyExistsException()
                            "username" -> return UsernameAlreadyExistsException()
                        }
                    }

                    // Additional codes can be handled here
                    when (code) {
                        "INVALID_CREDENTIALS" -> return InvalidCredentialsException()
                        "TOKEN_EXPIRED" -> return SessionExpiredException()
                    }
                }

                // Return specific error message if available
                if (message.isNotEmpty()) {
                    return ApiException("Error: $message")
                }
            }

            // Default to generic API error if no specific errors found
            ApiException("Error en el servidor")
        } catch (e: Exception) {
            // If JSON parsing fails, return a generic API error
            ApiException("Error en el servidor: ${e.message}")
        }
    }

    /**
     * Handle 401 errors specifically, which usually indicate authentication problems
     */
    private fun handleAuth401Error(errorBody: String): AuthException {
        return try {
            val jsonObject = JSONObject(errorBody)
            val errorsArray = jsonObject.optJSONArray("errors")

            if (errorsArray != null && errorsArray.length() > 0) {
                val errorObj = errorsArray.getJSONObject(0)
                val message = errorObj.optString("message", "").lowercase()

                when {
                    message.contains("token expired") ||
                            message.contains("invalid token") -> SessionExpiredException()

                    message.contains("credentials") ||
                            message.contains("invalid") -> InvalidCredentialsException()

                    else -> ApiException("Error de autenticación")
                }
            } else {
                InvalidCredentialsException()
            }
        } catch (e: Exception) {
            InvalidCredentialsException()
        }
    }
}
