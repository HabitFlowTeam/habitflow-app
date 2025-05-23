package com.example.habitflow_app.domain.models

import java.time.LocalDate

data class CalendarDayStatus(
    val date: LocalDate,
    val status: DayStatus
)

sealed class DayStatus {
    data object Completed : DayStatus()
    data object Partial : DayStatus()
    data object Failed : DayStatus()
    data object Future : DayStatus()
    data object NoHabits : DayStatus()
}
