package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName


data class HabitDayResponse(
    @SerializedName("habit_id") val habitId: String,
    @SerializedName("week_day_id") val weekDayId: String
)

data class UpdateHabitDaysRequest(
    val habitId: String,
    val days: List<String>
)

// Response para operaciones de actualización
data class HabitUpdateResponse(
    val success: Boolean,
    val updatedCount: Int
)