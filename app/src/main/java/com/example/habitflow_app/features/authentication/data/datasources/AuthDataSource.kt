package com.example.habitflow_app.features.authentication.data.datasources

import android.util.Log
import com.example.habitflow_app.core.network.DirectusApiService
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
     * @throws Exception if registration fails at any step
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
                throw Exception("Registration failed with code: ${registerResponse.code()}")
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

            // TODO: Save tokens to local storage

            // Extract user ID from token
            Log.d(TAG, "[Paso 4/5] Extrayendo ID del token...")
            val userId = extractUserIdFromToken(accessToken)
            Log.d(TAG, "[Paso 4/5] ID extraído: $userId")

            // Step 3: Create profile in profiles table with the same ID
            Log.d(TAG, "[Paso 5/5] Creando perfil...")
            val profileResponse = directusApiService.createProfile(
                CreateProfileRequest(
                    id = userId,
                    fullName = profile.fullName,
                    username = profile.username,
                    streak = profile.streak,
                    bestStreak = profile.bestStreak,
                    avatarUrl = profile.avatarUrl
                )
            )

            // Check if registration was successful (200 status code)
            if (profileResponse.isSuccessful) {
                Log.d(
                    TAG,
                    "[Paso 5/5] Perfil creado existosamente (${profileResponse.code()} ${profileResponse.message()})"
                )
            } else {
                Log.w(
                    TAG,
                    "[Paso 5/5] Error en registro: ${profileResponse.code()} ${profileResponse.message()}"
                )
                throw Exception("Registration failed with code: ${profileResponse.code()}")
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
            throw Exception("API Error: ${e.message}")

        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw Exception("Connection Error")

        } catch (e: Exception) {
            Log.e(TAG, "ERROR inesperado:", e)
            throw Exception("Registration Error: ${e.message}")
        }
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token The JWT token
     * @return The user ID
     */
    private fun extractUserIdFromToken(token: String): String {
        try {
            Log.d(TAG, "Analizando token JWT...")

            // JWT tokens are in the format: header.payload.signature
            val parts = token.split(".").also {
                Log.d(TAG, "Partes del token: ${it.size} (se esperaban 3)")
            }
            if (parts.size != 3) {
                throw Exception("Invalid token format")
            }

            // Decode the payload (second part)
            val payload = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
                .toString(Charsets.UTF_8)
                .also {
                    Log.d(TAG, "Payload decodificado (primeros 50 chars): ${it.take(50)}...")
                }

            // Parse the JSON to extract the user ID
            val regex = "\"id\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val matchResult = regex.find(payload)

            return matchResult?.groupValues?.get(1)?.also {
                Log.d(TAG, "ID encontrado en token: $it")
            } ?: throw Exception("Could not extract user ID from token")

        } catch (e: Exception) {
            Log.e(TAG, "Fallo al extraer ID del token:", e)
            throw e
        }
    }

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param email User's email address
     * @param password User's password
     * @return Authenticated [User] object
     */
    suspend fun login(loginRequest: LoginRequest): User {
        try {
            Log.d(TAG, "Iniciando login para: ${loginRequest.email}")
            val response = directusApiService.login(loginRequest)
            Log.d(TAG, "Login exitoso. Token recibido")
            return User(
                id = extractUserIdFromToken(response.data.access_token),
                email = loginRequest.email,
                password = "",
                role = "authenticated"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error en login:", e)
            throw Exception("Login Error: ${e.message}")
        }
    }

    /** Terminates the current authenticated session. */
    suspend fun logout() {
        try {
            Log.d(TAG, "Ejecutando logout")
            directusApiService.logout()
            Log.d(TAG, "Logout completado")
        } catch (e: Exception) {
            Log.e(TAG, "Error en logout:", e)
        }
    }

    /**
     * Initiates password reset flow for the given email.
     *
     * @param email Email address associated with the account
     */
    suspend fun resetPassword(email: String) {
        TODO("Implement password reset functionality")
    }
}
