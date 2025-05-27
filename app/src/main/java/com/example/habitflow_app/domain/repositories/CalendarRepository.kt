package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.DayStatus
import java.time.LocalDate

/**
 * Repository interface for managing calendar data.
 * This interface defines the contract for fetching calendar status data.
 */
interface CalendarRepository {
    suspend fun getCalendarStatusMap(
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<LocalDate, DayStatus>
}
