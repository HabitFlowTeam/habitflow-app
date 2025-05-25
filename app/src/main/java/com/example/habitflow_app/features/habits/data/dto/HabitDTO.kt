package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName

data class HabitsResponse(
    @SerializedName("data")
    val habits: List<ActiveHabitDto>
)

data class ActiveHabitDto(
    @SerializedName("habit_id")
    val habitId: String,

    @SerializedName("habit_name")
    val name: String,

    val id: String,

    @SerializedName("is_checked_today")
    val isCheckedToday: Boolean,

    @SerializedName("scheduled_days")
    val scheduledDays: List<String>,

    val streak: Int,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("habit_tracking_id")
    val habitTrackingId: String? = null,
)