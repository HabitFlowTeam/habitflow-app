package com.example.habitflow_app.features.habits.data.dto

import android.util.Log
import com.example.habitflow_app.domain.models.HabitTracking
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HabitTrackingResponseDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("is_checked")
    val isChecked: Boolean = false,

    @SerializedName("tracking_date")
    val trackingDate: String,

    @SerializedName("habit_id")
    val habitId: String
) : Serializable {
    fun toDomainModel(): HabitTracking {
        // Agregar logs para debug
        Log.d("HabitTrackingDTO", "Converting to domain model:")
        Log.d("HabitTrackingDTO", "id: $id")
        Log.d("HabitTrackingDTO", "habitId: $habitId")
        Log.d("HabitTrackingDTO", "date: $trackingDate")
        Log.d("HabitTrackingDTO", "isChecked: $isChecked")

        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val localDate = try {
            LocalDate.parse(trackingDate, dateFormatter)
        } catch (e: Exception) {
            Log.e("HabitTrackingDTO", "Error parsing date: ${e.message}")
            LocalDate.now() // Default value if parsing fails
        }

        return HabitTracking(
            id = id,
            isChecked = isChecked,
            trackingDate = localDate, habitId = habitId
        )
    }
}

@kotlinx.serialization.Serializable
data class UserHabitCategoriesViewDTO(
    val tracking_id: String?,
    val habit_id: String?,
    val habit_name: String?,
    val streak: Int?,
    val notifications_enable: Boolean?,
    val reminder_time: String?,
    val is_deleted: Boolean?,
    val created_at: String?,
    val expiration_date: String?,
    val category_id: String?,
    val user_id: String?,
    val is_checked: Boolean?,
     val tracking_date: String?,
    val category_name: String?
)

@kotlinx.serialization.Serializable
data class UserHabitCategoriesViewResponse(
    val data: List<UserHabitCategoriesViewDTO>
)
