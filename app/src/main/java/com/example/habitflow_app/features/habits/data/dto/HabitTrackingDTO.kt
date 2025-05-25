package com.example.habitflow_app.features.habits.data.dto

import com.example.habitflow_app.domain.models.HabitTracking
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class HabitTrackingResponseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("is_checked")
    val isChecked: Boolean = false,

    @SerializedName("tracking_date")
    val trackingDate: LocalDate,

    @SerializedName("habit_id")
    val habitId: String
) : Serializable {
    fun toDomainModel() = HabitTracking(
        id = id,
        isChecked = isChecked,
        trackingDate = trackingDate,
        habitId = habitId
    )
}
