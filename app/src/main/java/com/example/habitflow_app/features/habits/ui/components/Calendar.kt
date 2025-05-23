package com.example.habitflow_app.features.habits.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.domain.models.DayStatus
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun HorizontalCalendarPreview() {
    val today = LocalDate.now()
    val dayStatuses = mapOf(
        today.minusDays(3) to DayStatus.Partial,
        today.minusDays(2) to DayStatus.Failed,
        today.minusDays(1) to DayStatus.Completed,
        today to DayStatus.Completed,
        today.plusDays(1) to DayStatus.Future,
        today.plusDays(2) to DayStatus.Future,
        today.plusDays(3) to DayStatus.Future
    )

    HabitflowAppTheme {
        Calendar(
            currentDate = today,
            dayStatuses = dayStatuses,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * A composable that displays a horizontal calendar showing a range of days
 * centered around the current date, indicating the completion status of each day.
 *
 * @param currentDate The reference date from which the calendar range is calculated.
 * @param dayStatuses A map associating dates with their respective status (completed, partial, failed, or future).
 * @param modifier An optional [Modifier] for layout customization.
 */
@Composable
fun Calendar(
    currentDate: LocalDate = LocalDate.now(),
    dayStatuses: Map<LocalDate, DayStatus> = emptyMap(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val spanishLocale = Locale("es", "ES")

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hoy",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (-3..3).forEach { offset ->
                val date = today.plusDays(offset.toLong())
                val dayName = date.dayOfWeek.getDisplayName(
                    TextStyle.SHORT,
                    spanishLocale
                ).replace(".", "").replaceFirstChar { it.uppercase() }

                Text(
                    text = dayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (-3..3).forEach { offset ->
                val date = today.plusDays(offset.toLong())
                val isToday = date == today
                val isPast = date.isBefore(today)
                val status = dayStatuses[date] ?: DayStatus.Future

                DayCircle(
                    day = date.dayOfMonth,
                    isToday = isToday,
                    isPast = isPast,
                    status = status
                )
            }
        }
    }
}

/**
 * A composable that visually represents a day in the calendar as a circle,
 * optionally showing an icon based on the day's status.
 *
 * @param day The day number (1–31) to display inside the circle.
 * @param isToday True if the day represents the current date.
 * @param isPast True if the day is before today.
 * @param status The status of the day (completed, partial, failed, or future).
 */
@Composable
private fun DayCircle(
    day: Int,
    isToday: Boolean,
    isPast: Boolean,
    status: DayStatus
) {
    val (icon, backgroundColor, contentColor) = when {
        isToday -> Triple(
            null,
            when (status) {
                is DayStatus.Completed -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                is DayStatus.Partial -> Color(0xFFFF9800).copy(alpha = 0.2f)
                is DayStatus.Failed -> Color(0xFFF44336).copy(alpha = 0.2f)
                else -> Color.Transparent
            },
            MaterialTheme.colorScheme.onSurface
        )
        isPast -> Triple(
            when (status) {
                is DayStatus.Completed -> Icons.Default.Check
                is DayStatus.Partial -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                is DayStatus.Failed -> Icons.Default.Close
                else -> null
            },
            when (status) {
                is DayStatus.Completed -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                is DayStatus.Partial -> Color(0xFFFF9800).copy(alpha = 0.2f)
                is DayStatus.Failed -> Color(0xFFF44336).copy(alpha = 0.2f)
                else -> Color.Transparent
            },
            when (status) {
                is DayStatus.Completed -> Color(0xFF4CAF50)
                is DayStatus.Partial -> Color(0xFFFF9800)
                is DayStatus.Failed -> Color(0xFFF44336)
                else -> Color.Transparent
            }
        )
        else -> Triple(
            null,
            Color(0xFF9E9E9E).copy(alpha = 0.2f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (isPast) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = when (status) {
                        is DayStatus.Completed -> "Completado"
                        is DayStatus.Partial -> "Parcial"
                        is DayStatus.Failed -> "Fallado"
                        else -> null
                    },
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Text(
                text = day.toString(),
                color = if (isToday) MaterialTheme.colorScheme.primary else contentColor,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (isToday) 16.sp else 14.sp
            )
        }
    }
}