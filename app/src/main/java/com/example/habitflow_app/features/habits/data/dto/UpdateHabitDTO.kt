package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName


data class HabitUpdateRequest(
    val name: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("reminder_time") val reminderTime: String? = null,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean? = null,

    val days: List<HabitDayUpdateRequest>? = null
)

data class HabitDayUpdateRequest(
    @SerializedName("id") val id: String?,
    @SerializedName("week_day_id") val weekDayId: String
)