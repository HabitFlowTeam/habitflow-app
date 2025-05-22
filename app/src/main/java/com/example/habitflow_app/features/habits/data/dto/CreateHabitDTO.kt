package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalTime


data class CreateHabitRequest(
    val name: String,
    val categoryId: String,
    val selectedDays: List<String>,
    val reminderTime: LocalTime?,
    val initialTracking: Boolean = true
)

data class HabitResponse(
    val name: String,
    val categoryId: String,
    val selectedDays: List<String>,
    val reminderTime: LocalTime?
)

data class HabitApiRequest(
    val name: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("reminder_time") val reminderTime: String?,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean
)

data class HabitDayApiRequest(
    @SerializedName("habit_id") val habitId: String,
    @SerializedName("week_day_id") val weekDayId: String
)

data class HabitTrackingApiRequest(
    @SerializedName("is_checked") val isChecked: Boolean,
    @SerializedName("tracking_date") val trackingDate: String,
    @SerializedName("habit_id") val habitId: String
)


data class HabitUpdateRequest(
    val name: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("reminder_time") val reminderTime: String? = null,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean? = null,
    val streak: Int? = null,

    val days: List<HabitDayUpdateRequest>? = null
)

data class HabitDayUpdateRequest(
    @SerializedName("id") val id: String?,
    @SerializedName("week_day_id") val weekDayId: String
)