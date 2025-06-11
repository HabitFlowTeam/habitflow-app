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
            LocalDate.now()
        }

        return HabitTracking(
            id = id,
            isChecked = isChecked,
            trackingDate = localDate,
            habitId = habitId
        )
    }
}

data class UserHabitCategoriesViewDTO(
    @SerializedName("tracking_id")
    val trackingId: String?,

    @SerializedName("habit_id")
    val habitId: String?,

    @SerializedName("habit_name")
    val habitName: String?,

    @SerializedName("streak")
    val streak: Int?,

    @SerializedName("notifications_enable")
    val notificationsEnable: Boolean?,

    @SerializedName("reminder_time")
    val reminderTime: String?,

    @SerializedName("is_deleted")
    val isDeleted: Boolean?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("expiration_date")
    val expirationDate: String?,

    @SerializedName("category_id")
    val categoryId: String?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("is_checked")
    val isChecked: Boolean?,

    @SerializedName("tracking_date")
    val trackingDate: String?,

    @SerializedName("category_name")
    val categoryName: String?
)

data class UserHabitCategoriesViewResponse(
    @SerializedName("data")
    val data: List<UserHabitCategoriesViewDTO>
)

data class HabitScheduledDayDTO(
    @SerializedName("day_name")
    val dayName: String
)

data class HabitTrackingDateDTO(
    @SerializedName("is_checked")
    val isChecked: Boolean
)