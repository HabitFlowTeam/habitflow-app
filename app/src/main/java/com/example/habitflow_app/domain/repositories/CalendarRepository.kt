package com.example.habitflow_app.domain.repositories

import com.example.habitflow_app.domain.models.DayStatus
import java.time.LocalDate

interface CalendarRepository {
    suspend fun getCalendarStatusMap(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<LocalDate, DayStatus>
}
