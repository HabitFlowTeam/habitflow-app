package com.example.habitflow_app.domain.datasources

import com.example.habitflow_app.features.habits.data.dto.CalendarDayDto
import java.time.LocalDate

interface CalendarRemoteDataSource {
    suspend fun getCalendar(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CalendarDayDto>
}
