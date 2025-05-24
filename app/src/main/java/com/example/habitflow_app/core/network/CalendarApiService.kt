package com.example.habitflow_app.core.network

import com.example.habitflow_app.features.habits.data.dto.CalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CalendarApiService is an interface that defines the API endpoints for calendar-related operations.
 * It uses Retrofit to make network requests.
 */
interface CalendarApiService {
    @GET("items/user_habit_calendar_view")
    suspend fun getUserCalendar(
        @Query("filter[calendar_date][_between]") dateRange: String
    ): CalendarResponseDto
}
