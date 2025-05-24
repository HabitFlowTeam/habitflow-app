package com.example.habitflow_app.domain.datasources

import com.example.habitflow_app.features.habits.data.dto.CalendarResponseDto
import java.time.LocalDate

/**
 * CalendarDataSource is an interface that defines the contract for fetching calendar data.
 * It provides a method to retrieve calendar information for a specified date range.
 */
interface CalendarDataSource {
    suspend fun getCalendar(
        startDate: LocalDate,
        endDate: LocalDate
    ): CalendarResponseDto
}
