package com.example.habitflow_app.domain.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate

/**
 * Represents the status of a day in the habit tracking calendar.
 * This can be used to determine how to display the day in the UI.
 */
sealed class DayStatus {
    data object Completed : DayStatus()
    data object Partial : DayStatus()
    data object Failed : DayStatus()
    data object Future : DayStatus()
    data object NoHabits : DayStatus()
}

// Data class to represent the style of a day in the calendar
data class DayStyle(
    val icon: ImageVector?,
    val backgroundColor: Color,
    val contentColor: Color
)

// Data class to cache daily information
data class DayInfo(
    val date: LocalDate,
    val dayNumber: Int,
    val dayName: String,
    val isToday: Boolean,
    val isPast: Boolean,
    val status: DayStatus
)