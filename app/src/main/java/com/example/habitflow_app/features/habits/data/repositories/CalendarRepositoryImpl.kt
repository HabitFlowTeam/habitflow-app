package com.example.habitflow_app.features.habits.data.repositories

import com.example.habitflow_app.domain.datasources.CalendarDataSource
import com.example.habitflow_app.domain.models.DayStatus
import com.example.habitflow_app.domain.repositories.CalendarRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * CalendarRepositoryImpl is an implementation of the CalendarRepository interface.
 * It uses the CalendarDataSource to fetch calendar data from a remote source.
 *
 * @param remoteDataSource The data source used to fetch calendar data.
 */
class CalendarRepositoryImpl @Inject constructor(
    private val remoteDataSource: CalendarDataSource
) : CalendarRepository {

    private val formatter = DateTimeFormatter.ISO_DATE

    /**
     * Fetches the calendar status map for a specified date range.
     *
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A map where the key is a LocalDate and the value is a DayStatus.
     */
    override suspend fun getCalendarStatusMap(
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<LocalDate, DayStatus> {
        val response =
            remoteDataSource.getCalendar(startDate, endDate)

        return response.data.associate { day ->
            val date = LocalDate.parse(day.calendar_date, formatter)
            val status = when (day.completion_status) {
                "all_completed" -> DayStatus.Completed
                "partially_completed" -> DayStatus.Partial
                "none_completed" -> DayStatus.Failed
                "no_habits" -> DayStatus.NoHabits
                else -> DayStatus.Future
            }
            date to status
        }
    }
}