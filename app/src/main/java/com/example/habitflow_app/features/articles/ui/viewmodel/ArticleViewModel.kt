package com.example.habitflow_app.features.articles.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.usecases.GetUserTopLikedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.habitflow_app.domain.models.RankedArticle
import com.example.habitflow_app.domain.usecases.GetRankedArticles
import com.example.habitflow_app.domain.usecases.GetAllArticlesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.habitflow_app.domain.usecases.LikeArticleUseCase
import com.example.habitflow_app.domain.usecases.UnlikeArticleUseCase
import com.example.habitflow_app.domain.usecases.IsArticleLikedUseCase

/**
 * ViewModel for managing and exposing article-related UI state.
 *
 * This ViewModel interacts with the use cases to retrieve articles, manage likes,
 * and exposes state flows for the UI to observe articles, loading, and error states.
 *
 * @property getUserTopLikedArticlesUseCase Use case to fetch top liked articles for a user
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase,
    private val getRankedArticle: GetRankedArticles,
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val likeArticleUseCase: LikeArticleUseCase,
    private val unlikeArticleUseCase: UnlikeArticleUseCase,
    private val isArticleLikedUseCase: IsArticleLikedUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ArticleViewModel"
    }

    private val _profileArticles = MutableStateFlow<List<ProfileArticle>>(emptyList())
    val profileArticles: StateFlow<List<ProfileArticle>> = _profileArticles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // --- Artículos destacados (Ranked) ---
    private val _rankedArticles = MutableStateFlow<List<RankedArticle>>(emptyList())
    val rankedArticles: StateFlow<List<RankedArticle>> = _rankedArticles

    private val _rankedIsLoading = MutableStateFlow(false)
    val rankedIsLoading: StateFlow<Boolean> = _rankedIsLoading

    private val _rankedError = MutableStateFlow<String?>(null)
    val rankedError: StateFlow<String?> = _rankedError

    // --- Todos los artículos ---
    private val _allArticles = MutableStateFlow<List<RankedArticle>>(emptyList())
    val allArticles: StateFlow<List<RankedArticle>> = _allArticles

    private val _allArticlesIsLoading = MutableStateFlow(false)
    val allArticlesIsLoading: StateFlow<Boolean> = _allArticlesIsLoading

    private val _allArticlesError = MutableStateFlow<String?>(null)
    val allArticlesError: StateFlow<String?> = _allArticlesError

    // --- Artículo seleccionado ---
    private val _selectedArticle = MutableStateFlow<RankedArticle?>(null)
    val selectedArticle: StateFlow<RankedArticle?> = _selectedArticle

    // Like-related state
    private val _isLiked = MutableStateFlow<Boolean?>(null)
    val isLiked: StateFlow<Boolean?> = _isLiked

    /**
     * Loads the top liked articles for the specified user and updates the UI state.
     *
     * @param userId The ID of the user whose articles are to be loaded
     */
    fun loadUserArticles(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = getUserTopLikedArticlesUseCase(userId)
                _profileArticles.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchRankedArticles() {
        _rankedIsLoading.value = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    getRankedArticle()
                }
                Log.e(TAG, "This is the result: $response")
                _rankedArticles.value = response
                _rankedError.value = null
            } catch (e: Exception) {
                _rankedError.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _rankedIsLoading.value = false
            }
        }
    }

    fun fetchAllArticles() {
        _allArticlesIsLoading.value = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    getAllArticlesUseCase()
                }
                _allArticles.value = response
                _allArticlesError.value = null
            } catch (e: Exception) {
                _allArticlesError.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _allArticlesIsLoading.value = false
            }
        }
    }

    fun selectArticleById(articleId: String) {
        Log.e(TAG, "Selecting article with ID: $articleId")
        Log.e(TAG, "Articles available: ${_allArticles.value}")
        val article = _allArticles.value.find { it.articleId == articleId }
        _selectedArticle.value = article
    }

    fun checkLikeStatus(articleId: String) {
        if (articleId.isEmpty()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            isArticleLikedUseCase(articleId)
                .onSuccess { liked ->
                    _isLiked.value = liked
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }

    fun toggleLike(articleId: String) {
        Log.d(TAG, "PASO 1: Iniciando operación de toggle like")
        if (articleId.isEmpty()) {
            Log.e(TAG, "PASO 1.1: Error - articleId está vacío")
            return
        }
        
        viewModelScope.launch {
            Log.d(TAG, "PASO 2: Iniciando coroutine para toggle like")
            _isLoading.value = true
            val currentLikeState = _isLiked.value
            Log.d(TAG, "PASO 2.1: Estado actual del like: $currentLikeState")
            
            val result = if (currentLikeState == true) {
                Log.d(TAG, "PASO 3: Ejecutando unlike - llamando a unlikeArticleUseCase")
                unlikeArticleUseCase(articleId)
            } else {
                Log.d(TAG, "PASO 3: Ejecutando like - llamando a likeArticleUseCase")
                likeArticleUseCase(articleId)
            }

            result
                .onSuccess { success ->
                    Log.d(TAG, "PASO 4: Resultado de la operación: $success")
                    if (success) {
                        _isLiked.value = !currentLikeState!!
                        _selectedArticle.value = _selectedArticle.value?.let { article ->
                            article.copy(
                                likesCount = article.likesCount + (if (currentLikeState) -1 else 1)
                            )
                        }
                        Log.d(TAG, "PASO 4.1: Estado actualizado - likes: ${_selectedArticle.value?.likesCount}, isLiked: ${_isLiked.value}")
                    } else {
                        _error.value = "No se pudo actualizar el estado del like"
                        Log.e(TAG, "PASO 4.2: Error - No se pudo actualizar el estado")
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    Log.e(TAG, "PASO 4.3: Error en la operación", exception)
                }
            
            _isLoading.value = false
            Log.d(TAG, "PASO 5: Operación de toggle like completada")
        }
    }

    fun clearError() {
        _error.value = null
    }
}
