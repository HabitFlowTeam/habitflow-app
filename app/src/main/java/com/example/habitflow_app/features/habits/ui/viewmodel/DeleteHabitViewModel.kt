package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.usecases.DeleteHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteHabitViewModel @Inject constructor(
    private val softDeleteHabitUseCase: DeleteHabitUseCase
) : ViewModel(){

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Idle)
    val deleteState: StateFlow<DeleteState> = _deleteState

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            try {
                val success = softDeleteHabitUseCase(habitId)
                _deleteState.value = if (success) {
                    DeleteState.Success
                } else {
                    DeleteState.Error("Failed to delete habit")
                }
            } catch (e: Exception) {
                _deleteState.value = DeleteState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class DeleteState {
        object Idle : DeleteState()
        object Loading : DeleteState()
        object Success : DeleteState()
        data class Error(val message: String) : DeleteState()
    }
}