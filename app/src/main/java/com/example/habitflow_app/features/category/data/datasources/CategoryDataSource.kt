package com.example.habitflow_app.features.category.data.datasources

import android.util.Log
import com.example.habitflow_app.core.exceptions.ApiException
import com.example.habitflow_app.core.exceptions.NetworkConnectionException
import com.example.habitflow_app.core.network.DirectusApiService
import com.example.habitflow_app.domain.models.Category
import javax.inject.Inject

/**
 * Data source for fetching category data from the Directus API service.
 * Handles network and API exceptions.
 */
class CategoryDataSource @Inject constructor(
    private val directusApiService: DirectusApiService
) {
    private companion object {
        const val TAG = "CategoryDataSource"
    }

    /**
     * Retrieves the list of categories from the Directus service.
     *
     * @return List of [Category] objects fetched from the backend.
     * @throws ApiException if an API error or unexpected error occurs.
     * @throws NetworkConnectionException if a network connection error occurs.
     */
    suspend fun getCategories(): List<Category> {
        try {
            Log.d(TAG, "Obteniendo todas las categorías")
            val response = directusApiService.getCategories()

            if (!response.isSuccessful) {
                Log.e(TAG, "Error al obtener categorías: ${response.code()} ${response.message()}")
                throw ApiException("Error al obtener categorías: ${response.code()}")
            }

            val categories = response.body()?.data ?: emptyList()
            Log.d(TAG, "Categorías obtenidas exitosamente: ${categories.size} categorías")

            return categories.map {
                Category(
                    id = it.id,
                    name = it.name,
                    description = it.description
                )
            }
        } catch (e: retrofit2.HttpException) {
            Log.e(TAG, "ERROR HTTP ${e.code()}: ${e.message()}")
            throw ApiException("Error al obtener categorías: ${e.message()}")
        } catch (e: java.io.IOException) {
            Log.e(TAG, "ERROR de red: ${e.message}")
            throw NetworkConnectionException()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR inesperado:", e)
            throw ApiException("Error al obtener categorías: ${e.message}")
        }
    }
}
