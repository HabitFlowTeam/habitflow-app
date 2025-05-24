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

/**
 * ViewModel for managing and exposing article-related UI state.
 *
 * This ViewModel interacts with the use case to retrieve the top liked articles for a user,
 * and exposes state flows for the UI to observe articles, loading, and error states.
 *
 * @property getUserTopLikedArticlesUseCase Use case to fetch top liked articles for a user
 */
@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase
) : ViewModel() {
    private companion object {
        const val TAG = "ArticleViewModel"
    }
    private val _profileArticles = MutableStateFlow<List< ProfileArticle>>(emptyList())
    val profileArticles: StateFlow<List<ProfileArticle>> = _profileArticles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
                Log.e(TAG, "This is the result: $result")
                _profileArticles.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

