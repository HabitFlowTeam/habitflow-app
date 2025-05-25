package com.example.habitflow_app.features.habits.ui.extensions

import com.example.habitflow_app.features.habits.ui.viewmodel.HabitUiModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Extension functions for HabitUiModel to handle common operations
 */

/**
 * Determines if a habit is scheduled for today based on the current day of the week
 */
fun HabitUiModel.isScheduledForToday(): Boolean {
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return this.days.contains(currentDay, ignoreCase = true) || this.days == "Todos los días"
}

/**
 * Filters a list of habits to get only those scheduled for today
 */
fun List<HabitUiModel>.getTodayHabits(): List<HabitUiModel> {
    return this.filter { it.isScheduledForToday() }
}

/**
 * Filters a list of habits to get those NOT scheduled for today
 */
fun List<HabitUiModel>.getOtherHabits(): List<HabitUiModel> {
    return this.filter { !it.isScheduledForToday() }
}

/**
 * Gets the current day name in the default locale
 */
fun getCurrentDayName(): String {
    return LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
}