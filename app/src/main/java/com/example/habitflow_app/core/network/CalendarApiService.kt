package com.example.habitflow_app.core.network

import com.example.habitflow_app.features.habits.data.dto.CalendarResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarApiService {
    @GET("items/user_habit_calendar_view")
    suspend fun getUserCalendar(
        @Query("filter[user_id][_eq]") userId: String,
        @Query("filter[calendar_date][_between]") dateRange: String
    ): CalendarResponseDto
}
