package com.example.habitflow_app.features.articles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.ProfileArticle
import com.example.habitflow_app.domain.usecases.GetUserTopLikedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val getUserTopLikedArticlesUseCase: GetUserTopLikedArticlesUseCase
) : ViewModel() {
    private val _profileArticles = MutableStateFlow<List< ProfileArticle>>(emptyList())
    val profileArticles: StateFlow<List<ProfileArticle>> = _profileArticles

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

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
}

