package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName

data class HabitRequest(
    val name: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("reminder_time") val reminderTime: String? = null,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean = false,
    val streak: Int = 0,
    val days: List<HabitDayCreateRequest>
)

data class HabitDayCreateRequest(
    @SerializedName("week_day_id") val weekDayId: String
)