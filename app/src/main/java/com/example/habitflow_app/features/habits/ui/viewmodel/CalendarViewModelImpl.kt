package com.example.habitflow_app.features.habits.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitflow_app.domain.models.DayStatus
import com.example.habitflow_app.domain.repositories.CalendarRepository
import com.example.habitflow_app.domain.viewmodel.CalendarViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class CalendarViewModelImpl @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel(), CalendarViewModel {

    private val _calendarData = MutableStateFlow<Map<LocalDate, DayStatus>>(emptyMap())
    override val calendarData: StateFlow<Map<LocalDate, DayStatus>> = _calendarData.asStateFlow()

    private val _currentDate = MutableStateFlow(LocalDate.now())
    override val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Loads the initial calendar centered on the current date
     */
    override fun loadInitialData(centerDate: LocalDate) {
        val startDate = centerDate.minusDays(10)
        val endDate = centerDate.plusDays(3)
        loadCalendarForDateRange(startDate, endDate)
    }

    /**
     * Loads calendar data for a specific date range
     * This function is called when the user navigates through the calendar
     */
    override fun loadCalendarForDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val calendar = calendarRepository.getCalendarStatusMap(startDate, endDate)

                // Update calendar data
                _calendarData.value = _calendarData.value + calendar

                // Optional: update current date if we're navigating
                // Only update if we're not in the current week
                val today = LocalDate.now()
                val centerDate = startDate.plusDays(3) // Center of the 7-day range
                if (centerDate != today && (centerDate.isBefore(today.minusDays(3)) || centerDate.isAfter(
                        today.plusDays(3)
                    ))
                ) {
                    _currentDate.value = centerDate
                }

            } catch (e: Exception) {
                // Handle error - you could emit an error state
                // For now just log the error
                println("Error loading calendar data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Convenience function to load data when the user swipes the calendar
     */
    override fun onDateRangeChanged(startDate: LocalDate, endDate: LocalDate) {
        loadCalendarForDateRange(startDate, endDate)
    }

    /**
     * Refreshes calendar data for the current range
     * Useful for pull-to-refresh or when a habit is updated
     */
    override fun refreshCalendar() {
        val currentCenter = _currentDate.value
        val startDate = currentCenter.minusDays(3)
        val endDate = currentCenter.plusDays(3)
        loadCalendarForDateRange(startDate, endDate)
    }

    /**
     * Navigates to a specific date
     */
    override fun navigateToDate(date: LocalDate) {
        val startDate = date.minusDays(3)
        val endDate = date.plusDays(3)
        loadCalendarForDateRange(startDate, endDate)
    }

    /**
     * Navigates to the current date (today)
     */
    override fun navigateToToday() {
        navigateToDate(LocalDate.now())
    }
}