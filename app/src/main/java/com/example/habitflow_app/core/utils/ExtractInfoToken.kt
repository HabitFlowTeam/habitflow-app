package com.example.habitflow_app.core.utils

import android.util.Log
import javax.inject.Inject

private const val TAG = "TokenExtractor"

/**
 * Utility class for extracting information from JWT tokens.
 */
class ExtractInfoToken @Inject constructor() {

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token The JWT token
     * @return The user ID
     * @throws Exception if token is invalid or ID can't be extracted
     */
    fun extractUserIdFromToken(token: String): String {
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

}
