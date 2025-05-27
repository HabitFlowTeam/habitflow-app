package com.example.habitflow_app.features.habits.data.datasources

import com.example.habitflow_app.core.network.CalendarApiService
import com.example.habitflow_app.domain.datasources.CalendarDataSource
import com.example.habitflow_app.features.habits.data.dto.CalendarResponseDto
import java.time.LocalDate
import javax.inject.Inject

/**
 * CalendarDataSourceImpl is an implementation of the CalendarDataSource interface.
 * It uses the CalendarApiService to fetch calendar data from directus.
 *
 * @param apiService The API service used to make network requests.
 */
class CalendarDataSourceImpl @Inject constructor(
    private val apiService: CalendarApiService
) : CalendarDataSource {

    /**
     * Fetches the calendar data for a specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A CalendarResponseDto containing the calendar data.
     */
    override suspend fun getCalendar(
        startDate: LocalDate,
        endDate: LocalDate
    ): CalendarResponseDto {
        val formattedStart = startDate.toString()
        val formattedEnd = endDate.toString()
        val dateRange = "$formattedStart,$formattedEnd"

        return apiService.getUserCalendar(
            dateRange = dateRange
        )
    }
}
