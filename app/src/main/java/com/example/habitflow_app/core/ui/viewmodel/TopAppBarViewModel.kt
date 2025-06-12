package com.example.habitflow_app.core.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.repositories.ProfileRepository
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.core.utils.ExtractInfoToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAppBarViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val extractInfoToken: ExtractInfoToken
) : ViewModel() {
    var streak by mutableStateOf(0)
        private set

    init {
        loadUserStreak()
    }

    private fun loadUserStreak() {
        viewModelScope.launch {
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                val userId = extractInfoToken.extractUserIdFromToken(accessToken.toString())
                val profile = profileRepository.getProfile(userId)
                streak = profile.streak
            } catch (e: Exception) {
                // Handle error if needed
                e.printStackTrace()
            }
        }
    }
} 