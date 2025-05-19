package com.example.habitflow_app.domain.models

import java.time.LocalDate
import java.time.LocalTime

/**
 * Domain model for the habit.
 */
data class Habit(
    val id: String,
    val name: String,
    val streak: Int = 0,
    val notificationsEnabled: Boolean = false,
    val reminderTime: LocalTime?,
    val isDeleted: Boolean = false,
    val expirationDate: LocalDate,
    val categoryId: String,
    val userId: String
)

/**
 * Junction model representing which days of the week a habit should be active.
 */
data class HabitDay(
    val habitId: String,
    val weekDayId: String
)

/**
 * Domain model representing a habit tracking entry.
 */
data class HabitTracking(
    val id: String,
    val isChecked: Boolean = false,
    val trackingDate: LocalDate,
    val habitId: String
)

data class Category(
    val id: String,
    val name: String,
    val description: String
)

data class CategoriesResponse(
    val data: List<CategoryResponse>
)

data class CategoryResponse(
    val id: String,
    val name: String,
    val description: String
)