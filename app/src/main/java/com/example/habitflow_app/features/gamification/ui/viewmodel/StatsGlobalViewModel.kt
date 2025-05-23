package com.example.habitflow_app.features.gamification.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.LeaderboardUser
import com.example.habitflow_app.domain.usecases.GetCategoriesUseCase
import com.example.habitflow_app.domain.usecases.GetGlobalRankingUseCase
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling global statistics and leaderboard logic.
 * Manages user ranking data, categories selection, and loading states.
 *
 * @property getGlobalRankingUseCase Use case for fetching leaderboard data
 * @property getCategoriesUseCase Use case for fetching available categories
 * @property localDataStore Local storage for user session data
 * @property extractInfoToken Utility for extracting user info from tokens
 */
@HiltViewModel
class StatsGlobalViewModel @Inject constructor(
    private val getGlobalRankingUseCase: GetGlobalRankingUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val localDataStore: LocalDataStore,
    private val extractInfoToken: ExtractInfoToken
) : ViewModel() {

    private companion object {
        const val TAG = "StatsGlobalViewModel"
    }

    // Estado para la lista de usuarios en el ranking
    private val _leaderboardUsers = MutableStateFlow<List<LeaderboardUser>>(emptyList())
    val leaderboardUsers: StateFlow<List<LeaderboardUser>> = _leaderboardUsers.asStateFlow()

    // Estado para la categoría seleccionada
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Estado para indicar si está cargando
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado para el ID del usuario actual
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    // Estado para la lista de categorías disponibles
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    // Inicializar cargando el ranking global, el ID del usuario actual y las categorías
    init {
        loadCurrentUserId()
        loadCategories()
        loadGlobalRanking()
    }

    /**
     * Loads the current user ID from the stored authentication token.
     * Updates the [_currentUserId] state flow with the extracted ID.
     */
    private fun loadCurrentUserId() {
        viewModelScope.launch {
            try {
                val token = localDataStore.getAccessTokenOnce()
                if (token != null) {
                    val userId = extractInfoToken.extractUserIdFromToken(token)
                    _currentUserId.value = userId
                    Log.d(TAG, "ID de usuario actual cargado: $userId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar ID de usuario actual", e)
            }
        }
    }

    /**
     * Loads available categories from the backend.
     * Updates the [_categories] state flow with the fetched data.
     * Includes "Todos" as the first option followed by the actual categories.
     */
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                // Obtener las categorías desde el caso de uso
                val categoryList = getCategoriesUseCase()
                // Añadir "Todos" como primera opción y luego las categorías obtenidas
                _categories.value = listOf("Todos") + categoryList.map { it.name }
                Log.d(TAG, "Categorías cargadas: ${categoryList.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar categorías", e)
                _error.value = "Error al cargar categorías: ${e.message}"
            }
        }
    }

    /**
     * Loads the global ranking based on the currently selected category.
     * Updates the [_leaderboardUsers] state flow with the fetched data.
     * Manages loading and error states during the operation.
     */
    fun loadGlobalRanking() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val category = _selectedCategory.value
                val users = getGlobalRankingUseCase(category)

                _leaderboardUsers.value = users

                Log.d(TAG, "Ranking cargado exitosamente: ${users.size} usuarios")
            } catch (e: Exception) {
                Log.e(TAG, "Error al cargar ranking", e)
                _error.value = "Error al cargar ranking: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates the selected category and triggers a ranking reload.
     *
     * @param category The new category to filter by (null for all categories)
     */
    fun selectCategory(category: String?) {
        if (_selectedCategory.value != category) {
            _selectedCategory.value = category
            loadGlobalRanking()
        }
    }
}
