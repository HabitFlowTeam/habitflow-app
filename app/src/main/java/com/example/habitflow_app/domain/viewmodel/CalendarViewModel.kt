package com.example.habitflow_app.domain.viewmodel

import com.example.habitflow_app.domain.models.DayStatus
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface CalendarViewModel {

    val calendarData: StateFlow<Map<LocalDate, DayStatus>>
    val currentDate: StateFlow<LocalDate>
    val isLoading: StateFlow<Boolean>

    /**
     * Loads the initial calendar centered on the current date
     */
    fun loadInitialData(centerDate: LocalDate = LocalDate.now())

    /**
     * Loads calendar data for a specific date range
     * This function is called when the user navigates through the calendar
     */
    fun loadCalendarForDateRange(startDate: LocalDate, endDate: LocalDate)

    /**
     * Convenience function to load data when the user swipes the calendar
     */
    fun onDateRangeChanged(startDate: LocalDate, endDate: LocalDate)

    /**
     * Refreshes calendar data for the current range
     * Useful for pull-to-refresh or when a habit is updated
     */
    fun refreshCalendar()

    /**
     * Navigates to a specific date
     */
    fun navigateToDate(date: LocalDate)

    /**
     * Navigates to the current date (today)
     */
    fun navigateToToday()
}