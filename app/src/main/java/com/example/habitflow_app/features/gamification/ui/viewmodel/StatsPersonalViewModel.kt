package com.example.habitflow_app.features.gamification.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.core.utils.ExtractInfoToken
import com.example.habitflow_app.domain.models.HabitWithCategory
import com.example.habitflow_app.domain.repositories.AuthRepository
import com.example.habitflow_app.domain.usecases.GetUserHabitCategoriesUseCase
import com.example.habitflow_app.domain.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDate

@HiltViewModel
class StatsPersonalViewModel @Inject constructor(
    private val getUserHabitCategoriesUseCase: GetUserHabitCategoriesUseCase,
    private val profileRepository: ProfileRepository,
    private val extractInfoToken: ExtractInfoToken,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<HabitWithCategory>>(emptyList())
    val habits: StateFlow<List<HabitWithCategory>> = _habits.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estadísticas calculadas
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _bestStreak = MutableStateFlow(0)
    val bestStreak: StateFlow<Int> = _bestStreak.asStateFlow()

    private val _completedHabits = MutableStateFlow(0)
    val completedHabits: StateFlow<Int> = _completedHabits.asStateFlow()

    private val _weeklyCompletion = MutableStateFlow(0)
    val weeklyCompletion: StateFlow<Int> = _weeklyCompletion.asStateFlow()

    private val _activeDaysThisMonth = MutableStateFlow(0)
    val activeDaysThisMonth: StateFlow<Int> = _activeDaysThisMonth.asStateFlow()

    private val _mostFrequentHabit = MutableStateFlow("")
    val mostFrequentHabit: StateFlow<String> = _mostFrequentHabit.asStateFlow()

    private val _barChartData = MutableStateFlow<List<Int>>(emptyList())
    val barChartData: StateFlow<List<Int>> = _barChartData.asStateFlow()

    private val _pieChartData = MutableStateFlow<Map<String, Int>>(emptyMap())
    val pieChartData: StateFlow<Map<String, Int>> = _pieChartData.asStateFlow()

    fun loadStats() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val accessToken = authRepository.getAccessTokenOnce()
                if (accessToken.isNullOrBlank()) {
                    _error.value = "Autenticación requerida"
                    return@launch
                }
                // Obtener streaks del perfil
                val userId = extractInfoToken.extractUserIdFromToken(accessToken)
                val profile = profileRepository.getProfile(userId)
                _currentStreak.value = profile?.streak ?: 0
                _bestStreak.value = profile?.bestStreak ?: 0
                // Obtener hábitos y calcular el resto de estadísticas
                val data = getUserHabitCategoriesUseCase(userId)
                _habits.value = data
                calculateStats(data)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateStats(habits: List<HabitWithCategory>) {
        // Hábitos completados (trackings con isChecked true)
        _completedHabits.value = habits.count { it.isChecked == true }
        // Porcentaje de cumplimiento semanal (trackings de la última semana)
        val last7Days = habits.filter { it.trackingDate != null && isInLast7Days(it.trackingDate!!) }
        val checkedLast7Days = last7Days.count { it.isChecked == true }
        _weeklyCompletion.value = if (last7Days.isNotEmpty()) (checkedLast7Days * 100 / last7Days.size) else 0
        // Días activos este mes (días únicos con tracking)
        val daysThisMonth = habits.mapNotNull { it.trackingDate?.take(10) }
            .filter { isInCurrentMonth(it) }
            .toSet().size
        _activeDaysThisMonth.value = daysThisMonth
        // Hábito más frecuente (el nombre con más trackings)
        val mostFrequent = habits.groupBy { it.habitName }.maxByOrNull { it.value.size }?.key ?: ""
        _mostFrequentHabit.value = mostFrequent ?: ""
        // Datos para gráfica de barras (hábitos completados por día de la semana)
        val barData = (0..6).map { dayOfWeek ->
            habits.count { it.trackingDate?.let { date -> getDayOfWeek(date) == dayOfWeek && it.isChecked == true } == true }
        }
        _barChartData.value = barData
        // Datos para gráfica de pastel (conteo por categoría)
        val pieData = habits.groupBy { it.categoryName ?: "Otros" }.mapValues { it.value.size }
        _pieChartData.value = pieData
    }

    private fun isInLast7Days(date: String): Boolean {
        return try {
            val parsed = LocalDate.parse(date.take(10))
            val now = LocalDate.now()
            !parsed.isAfter(now) && !parsed.isBefore(now.minusDays(6))
        } catch (e: Exception) { false }
    }

    private fun isInCurrentMonth(date: String): Boolean {
        return try {
            val parsed = LocalDate.parse(date.take(10))
            val now = LocalDate.now()
            parsed.month == now.month && parsed.year == now.year
        } catch (e: Exception) { false }
    }

    private fun getDayOfWeek(date: String): Int {
        // 0 = Lunes, 6 = Domingo
        return try {
            val parsed = LocalDate.parse(date.take(10))
            val day = parsed.dayOfWeek.value
            if (day == 7) 6 else day - 1
        } catch (e: Exception) { 0 }
    }
}