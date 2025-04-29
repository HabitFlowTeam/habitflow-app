package com.example.habitflow_app.features.profile.data.datasources

import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.Profile
import javax.inject.Inject

class ProfileDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
) {
    suspend fun getProfile(userId: String): Profile {
        val response = directusApiService.getProfile(userId)
        if (response.isSuccessful) {
            return response.body()?.data?.toDomainModel()
                ?: throw Exception("Datos del perfil no disponibles")
        } else {
            throw Exception("Error al obtener el perfil: ${response.message()}")
        }
    }
}