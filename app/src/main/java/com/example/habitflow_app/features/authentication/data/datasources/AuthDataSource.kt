package com.example.habitflow_app.features.authentication.data.datasources

import android.util.Log
import com.example.habitflow_app.core.exceptions.*
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.Profile
import com.example.habitflow_app.domain.models.User
import com.example.habitflow_app.features.authentication.data.dto.*
import javax.inject.Inject

/**
 * Data source implementation for authentication operations using Directus API.
 * Handles direct communication with Directus services for auth-related operations.
 */
class AuthDataSource @Inject constructor(
    private val directusApiService: DirectusApiService,
    private val extractInfoToken: ExtractInfoToken,
    private val localDataStore: LocalDataStore
) {

    private companion object {
        const val TAG = "AuthDebug"
    }

    /**
     * Registers a new user in Directus and creates a profile.
     *
     * @param user User data to register
     * @param profile Profile data to create
     * @return Registered User object if successful
     * @throws AuthException if registration fails with specific error
     */
    suspend fun registerUser(user: User, profile: Profile): User {
        try {
            Log.d(TAG, "[Paso 1/5] Iniciando registro para: ${user.email}")

            // Step 1: Register user in directus_users table
            Log.d(TAG, "[Paso 2/5] Enviando petición de registro...")
            val registerResponse = directusApiService.registerUser(
                RegisterUserRequest(
                    email = user.email,
                    password = user.password,
                    role = user.role
                )
            )

            // Check if registration was successful (204 status code)
            if (registerResponse.isSuccessful) {
                Log.d(
                    TAG,
                    "[Paso 2/5] Registro exitoso (${registerResponse.code()} ${registerResponse.message()})"
                )
            } else {
                Log.w(
                    TAG,
                    "[Paso 2/5] Error en registro: ${registerResponse.code()} ${registerResponse.message()}"
                )
                val errorBody = registerResponse.errorBody()?.string() ?: "No error details"
                Log.e(TAG, "Error body: $errorBody")

                // Parse the error to get a specific exception
                throw AuthExceptionHandler.parseDirectusError(errorBody, registerResponse.code())
            }

            // Step 2: Authenticate to get access tokens
            Log.d(TAG, "[Paso 3/5] Autenticando usuario para obtener tokens...")
            val authResponse = directusApiService.login(
                LoginRequest(
                    email = user.email,
                    password = user.password
                )
            )

            // Access the nested access_token from the data object
            val accessToken = authResponse.data.access_token
            Log.d(
                TAG,
                "[Paso 3/5] Autenticación exitosa. Token recibido (primeros 10 chars): ${
                    accessToken.take(10)
                }..."
            )

            // Save tokens to local storage
            localDataStore.saveAccessToken(accessToken)

            // Extract user ID from token
            Log.d(TAG, "[Paso 4/5] Extrayendo ID del token...")
            val userId = extractInfoToken.extractUserIdFromToken(accessToken)
            Log.d(TAG, "[Paso 4/5] ID extraído: $userId")

            // Step 3: Create profile in profiles table with the same ID
            Log.d(TAG, "[Paso 5/5] Creando perfil...")
            val profileResponse = directusApiService.createProfile(
                CreateProfileRequest(
                    id = userId,
                    full_name = profile.fullName,
                    username = profile.username,
                    streak = profile.streak,
                    best_streak = profile.bestStreak,
                    avatar_url = profile.avatarUrl
                )
            )

            // Check if profile creation was successful (200 status code)
            if (profileResponse.isSuccessful) {
                Log.d(
                    TAG,
                    "[Paso 5/5] Perfil creado existosamente (${profileResponse.code()} ${profileResponse.message()})"
                )
            } else {
                Log.w(
                    TAG,
                    "[Paso 5/5] Error en creación de perfil: ${profileResponse.code()} ${profileResponse.message()}"
                )
                val errorBody = profileResponse.errorBody()?.string() ?: "No error details"
                Log.e(TAG, "Error body: $errorBody")

                // If profile creation fails, delete the user we just created to maintain consistency
                try {
                    directusApiService.deleteUser(userId)
                    Log.d(TAG, "Usuario eliminado tras fallo en creación de perfil")
                } catch (e: Exception) {
                    Log.e(TAG, "No se pudo eliminar el usuario tras error en perfil", e)
                }

                // Parse the error to determine if it's a username uniqueness issue
                val exception =
                    AuthExceptionHandler.parseDirectusError(errorBody, profileResponse.code())

                // If it's not specifically a username issue but we know we're creating a profile,
                // it's likely a username uniqueness issue
                if (exception is ApiException && errorBody.contains("username", ignoreCase = true)
                ) {
                    throw UsernameAlreadyExistsException()
                } else {
                    throw exception
                }
            }

            // Return the registered user with the ID
            return User(
                id = userId,
                email = user.email,
                password = "", // Don't store password in memory
                role = user.role
            ).also {
                Log.d(TAG, "Registro completado para: ${it.email} (ID: ${it.id})")
            }
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "no details"
            Log.e(TAG, "ERROR HTTP ${e.code()}: $errorBody")
            throw AuthExceptionHandler.parseDirectusError(errorBody, e.code())

        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw NetworkConnectionException()

        } catch (e: AuthException) {
            Log.e(TAG, "ERROR de autenticación: ${e.message}")
            throw e

        } catch (e: Exception) {
            Log.e(TAG, "ERROR inesperado:", e)
            throw ApiException("Error de registro: ${e.message}")
        }
    }

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param loginRequest DTO containing user credentials (email and password)
     * @return Authenticated [User] object
     * @throws AuthException if login fails with specific error
     */
    suspend fun login(loginRequest: LoginRequest): User {
        try {
            Log.d(TAG, "Iniciando login para: ${loginRequest.email}")
            val response = directusApiService.login(loginRequest)
            localDataStore.saveAccessToken(response.data.access_token)
            Log.d(TAG, "Login exitoso. Token recibido")
            return User(
                id = extractInfoToken.extractUserIdFromToken(response.data.access_token),
                email = loginRequest.email,
                password = "",
                role = "authenticated"
            )
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "no details"
            Log.e(TAG, "ERROR HTTP ${e.code()}: $errorBody")

            // Use HTTP code to help identify specific errors for login
            throw AuthExceptionHandler.parseDirectusError(errorBody, e.code())

        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw NetworkConnectionException()
        } catch (e: Exception) {
            Log.e(TAG, "Error en login:", e)
            throw ApiException("Error de inicio de sesión: ${e.message}")
        }
    }

    suspend fun requestPasswordReset(email: String) {
        try {
            Log.d(TAG, "Iniciando solicitud de reset de contraseña para: $email")

            val response = directusApiService.resetPassword(
                PasswordResetRequest(email = email)
            )

            if (!response.isSuccessful) {
                throw Exception("Error al solicitar reset de contraseña: ${response.code()}")
            }

            Log.d(TAG, "Solicitud de reset de contraseña enviada exitosamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error en requestPasswordReset:", e)
            throw Exception("Error al solicitar reset de contraseña: ${e.message}")
        }
    }

    /** Terminates the current authenticated session. */
    /**
     * Terminates the current authenticated session.
     *
     * @throws AuthException if logout fails
     */
    suspend fun logout() {
        try {
            Log.d(TAG, "Ejecutando logout")
            directusApiService.logout()
            Log.d(TAG, "Logout completado")
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "no details"
            Log.e(TAG, "ERROR HTTP ${e.code()}: $errorBody")
            throw AuthExceptionHandler.parseDirectusError(errorBody, e.code())
        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw NetworkConnectionException()
        } catch (e: Exception) {
            Log.e(TAG, "Error en logout:", e)
            throw ApiException("Error al cerrar sesión: ${e.message}")
        }
    }

    /**
     * Initiates password reset flow for the given email.
     *
     * @param email Email address associated with the account
     * @throws AuthException if password reset request fails
     */
    suspend fun resetPassword(email: String) {
        try {
            Log.d(TAG, "Solicitando restablecimiento de contraseña para: $email")
            // Implement actual API call here
            // TODO("Implement password reset functionality")
        } catch (e: retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string() ?: "no details"
            Log.e(TAG, "ERROR HTTP ${e.code()}: $errorBody")
            throw AuthExceptionHandler.parseDirectusError(errorBody, e.code())
        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw NetworkConnectionException()
        } catch (e: Exception) {
            Log.e(TAG, "Error en restablecimiento de contraseña:", e)
            throw ApiException("Error al restablecer contraseña: ${e.message}")
        }
    }
}
