package com.example.habitflow_app.features.articles.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.usecases.GetUserTopLikedArticlesUseCase
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
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

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase,
    private val getRankedArticle: GetRankedArticles,
    private val getAllArticlesUseCase: GetAllArticlesUseCase,
    private val likeArticleUseCase: LikeArticleUseCase,
    private val unlikeArticleUseCase: UnlikeArticleUseCase,
    private val isArticleLikedUseCase: IsArticleLikedUseCase,
    private val localDataStore: LocalDataStore,
    private val extractInfoToken: ExtractInfoToken
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

    // --- Artículos del usuario actual ---
    private val _userArticles = MutableStateFlow<List<RankedArticle>>(emptyList())
    val userArticles: StateFlow<List<RankedArticle>> = _userArticles

    private val _userArticlesIsLoading = MutableStateFlow(false)
    val userArticlesIsLoading: StateFlow<Boolean> = _userArticlesIsLoading

    private val _userArticlesError = MutableStateFlow<String?>(null)
    val userArticlesError: StateFlow<String?> = _userArticlesError

    private val _otherArticles = MutableStateFlow<List<RankedArticle>>(emptyList())
    val otherArticles: StateFlow<List<RankedArticle>> = _otherArticles

    // --- Artículo seleccionado ---
    private val _selectedArticle = MutableStateFlow<RankedArticle?>(null)
    val selectedArticle: StateFlow<RankedArticle?> = _selectedArticle

    // Like-related state
    private val _isLiked = MutableStateFlow<Boolean?>(null)
    val isLiked: StateFlow<Boolean?> = _isLiked

    private suspend fun getCurrentUserId(): String? {
        return try {
            val token = localDataStore.getAccessTokenOnce()
            token?.let { extractInfoToken.extractUserIdFromToken(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting current user ID", e)
            null
        }
    }

    private suspend fun getUserArticlesFromApi(userId: String): List<RankedArticle> {
        return try {
            Log.d(TAG, "Buscando artículos del usuario: $userId")

            val allArticles = getAllArticlesUseCase()

            val userArticles = allArticles.filter { article ->
                Log.d(TAG, "Comparando artículo: authorName=${article.authorName}, buscado userId=$userId")

                article.authorName.contains(userId) || article.authorName == userId
            }

            Log.d(TAG, "Encontrados ${userArticles.size} artículos del usuario")
            userArticles
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo artículos del usuario", e)
            emptyList()
        }
    }

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
                val currentUserId = getCurrentUserId()
                Log.d(TAG, "Current user ID: $currentUserId")

                val response = withContext(Dispatchers.IO) {
                    getAllArticlesUseCase()
                }
                _allArticles.value = response
                _allArticlesError.value = null

                if (currentUserId != null) {
                    val userArticlesList = response.filter { article ->
                        article.userId == currentUserId
                    }
                    val otherArticlesList = response.filter { article ->
                        article.userId != currentUserId
                    }

                    _userArticles.value = userArticlesList
                    _otherArticles.value = otherArticlesList

                    Log.d(TAG, "User articles: ${userArticlesList.size}, Other articles: ${otherArticlesList.size}")

                    userArticlesList.forEach { article ->
                        Log.d(TAG, "User article: id=${article.articleId}, title=${article.title}, userId=${article.userId}")
                    }
                } else {
                    _userArticles.value = emptyList()
                    _otherArticles.value = response
                }
            } catch (e: Exception) {
                _allArticlesError.value = "Error de red: ${e.localizedMessage}"
                Log.e(TAG, "Error fetching articles", e)
            } finally {
                _allArticlesIsLoading.value = false
            }
        }
    }

    fun refreshArticles() {
        fetchAllArticles()
    }

    fun selectArticleById(articleId: String) {
        Log.d(TAG, "Selecting article with ID: $articleId")
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
        if (articleId.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            val currentLikeState = _isLiked.value

            val result = if (currentLikeState == true) {
                unlikeArticleUseCase(articleId)
            } else {
                likeArticleUseCase(articleId)
            }

            result
                .onSuccess { success ->
                    if (success) {
                        _isLiked.value = !currentLikeState!!
                        _selectedArticle.value = _selectedArticle.value?.let { article ->
                            article.copy(
                                likesCount = article.likesCount + (if (currentLikeState) -1 else 1)
                            )
                        }
                    } else {
                        _error.value = "No se pudo actualizar el estado del like"
                    }
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
