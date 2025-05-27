package com.example.habitflow_app.core.utils

import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.util.Date

object JwtUtils {
    private const val TAG = "JWT_UTILS"

    fun extractClaims(token: String): Map<String, Any>? {
        val parts = token.split(".")

        if (parts.size != 3) return null

        val payload = parts[1]

        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val decodedString = String(decodedBytes)

        return try {
            val jsonObject = JSONObject(decodedString)
            val map = mutableMapOf<String, Any>()
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = jsonObject.get(key)
                map[key] = value
            }
            map
        } catch (e: Exception) {
            null
        }
    }

    fun isTokenExpired(token: String): Boolean {
        val claims = extractClaims(token) ?: run {
            return true
        }

        val expClaim = claims["exp"]

        val expirationTime = when (expClaim) {
            is Long -> expClaim
            is Int -> expClaim.toLong()
            is String -> expClaim.toLongOrNull().also {
                if (it == null) {
                    Log.w(TAG, "No se pudo convertir 'exp' a Long: $expClaim")
                }
            }

            else -> {
                Log.w(TAG, "Tipo no soportado para 'exp': ${expClaim?.javaClass}")
                return true
            }
        }

        if (expirationTime == null) return true

        val expirationDate = Date(expirationTime * 1000L)
        val now = Date(System.currentTimeMillis())

        return expirationDate.before(now)
    }
}