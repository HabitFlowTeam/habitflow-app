package com.example.habitflow_app.features.articles.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.Category
import com.example.habitflow_app.domain.usecases.CreateArticleUseCase
import com.example.habitflow_app.domain.usecases.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateArticleViewModel @Inject constructor(
    private val createArticleUseCase: CreateArticleUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "CreateArticleViewModel"
    }

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun updateSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val categoriesList = getCategoriesUseCase()
                _categories.value = categoriesList
                Log.d(TAG, "Categories loaded: ${categoriesList.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error loading categories", e)
                _error.value = "Error al cargar las categorías: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createArticle(context: Context) {
        val currentCategory = _selectedCategory.value
        if (!isFormValid() || currentCategory == null) {
            _error.value = "Por favor completa todos los campos obligatorios"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d(TAG, "Creating article with title: ${_title.value}")

                createArticleUseCase(
                    title = _title.value.trim(),
                    content = _content.value.trim(),
                    imageUri = _selectedImageUri.value,
                    categoryId = currentCategory.id,
                    context = context
                ).onSuccess { articleId ->
                    Log.d(TAG, "Article created successfully with ID: $articleId")
                    _isSuccess.value = true
                }.onFailure { exception ->
                    Log.e(TAG, "Error creating article", exception)
                    _error.value = "Error al crear el artículo: ${exception.message}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error creating article", e)
                _error.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isFormValid(): Boolean {
        return _title.value.isNotBlank() &&
                _content.value.isNotBlank() &&
                _selectedCategory.value != null
    }

    fun clearError() {
        _error.value = null
    }
}