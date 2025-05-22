package com.example.habitflow_app.features.habits.data.dto

import com.google.gson.annotations.SerializedName


data class HabitDayUpdateRequest(
    @SerializedName("id") val id: String?,
    @SerializedName("week_day_id") val weekDayId: String
)