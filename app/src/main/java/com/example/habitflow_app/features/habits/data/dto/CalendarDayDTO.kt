package com.example.habitflow_app.features.habits.data.dto

data class CalendarResponseDto(
    val data: List<CalendarDayDto>
)

data class CalendarDayDto(
    val calendar_date: String,
    val completed_habits: String,
    val completion_percentage: Int,
    val completion_status: String,
    val id: String,
    val total_scheduled_habits: String,
    val user_id: String
)
