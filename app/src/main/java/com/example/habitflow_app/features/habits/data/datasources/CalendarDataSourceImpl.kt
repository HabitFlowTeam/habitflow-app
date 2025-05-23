package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.core.network.CalendarApiService
import com.example.habitflow_app.domain.datasources.CalendarRemoteDataSource
import com.example.habitflow_app.features.habits.data.dto.CalendarDayDto
import java.time.LocalDate

class CalendarDataSourceImpl(
    private val apiService: CalendarApiService
) : CalendarRemoteDataSource {

    override suspend fun getCalendar(
        userId: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<CalendarDayDto> {
        val formattedStart = startDate.toString()
        val formattedEnd = endDate.toString()
        val dateRange = "$formattedStart,$formattedEnd"

        return apiService.getUserCalendar(
            userId = userId,
            dateRange = dateRange
        ).data

    }
}
