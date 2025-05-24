package com.example.habitflow_app.features.habits.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.domain.models.DayInfo
import com.example.habitflow_app.domain.models.DayStatus
import com.example.habitflow_app.domain.models.DayStyle
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.abs

@Preview(showBackground = true)
@Composable
fun HorizontalCalendarPreview() {
    val today = LocalDate.now()
    val dayStatuses = mapOf(
        today.minusDays(4) to DayStatus.NoHabits,
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
            onDateRangeChanged = { startDate, endDate ->
                Log.e("Date range", "Loading data from $startDate to $endDate")
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * A composable that displays a horizontal calendar showing a range of days
 * with swipe navigation support to load different date ranges.
 */
@Composable
fun Calendar(
    currentDate: LocalDate = LocalDate.now(),
    dayStatuses: Map<LocalDate, DayStatus> = emptyMap(),
    onDateRangeChanged: ((startDate: LocalDate, endDate: LocalDate) -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val spanishLocale = remember { Locale("es", "ES") }

    var centerDate by remember { mutableStateOf(currentDate) }
    var accumulatedDrag by remember { mutableFloatStateOf(0f) }

    // Use derivedStateOf to avoid unnecessary recompositions
    val isCurrentWeek by remember {
        derivedStateOf {
            centerDate.isEqual(today) ||
                    (centerDate.isAfter(today.minusDays(3)) && centerDate.isBefore(today.plusDays(4)))
        }
    }

    val displayCenterDate by remember {
        derivedStateOf {
            if (isCurrentWeek) today else centerDate
        }
    }

    // Precalculate day information to avoid repeated calculations
    val daysInfo by remember(displayCenterDate, dayStatuses) {
        derivedStateOf {
            (-3..3).map { offset ->
                val date = displayCenterDate.plusDays(offset.toLong())
                DayInfo(
                    date = date,
                    dayNumber = date.dayOfMonth,
                    dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, spanishLocale)
                        .replace(".", "")
                        .replaceFirstChar { it.uppercase() },
                    isToday = date == today,
                    isPast = date.isBefore(today),
                    status = dayStatuses[date] ?: DayStatus.Future
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(centerDate) { // Use centerDate as key to recreate only when it changes
                detectDragGestures(
                    onDragEnd = {
                        accumulatedDrag = 0f
                    }
                ) { _, dragAmount ->
                    accumulatedDrag += dragAmount.x
                    val threshold = 150f // Increase threshold for less sensitivity

                    if (abs(accumulatedDrag) > threshold) {
                        val newCenterDate = if (accumulatedDrag > 0) {
                            centerDate.minusDays(7)
                        } else {
                            centerDate.plusDays(7)
                        }

                        centerDate = newCenterDate
                        accumulatedDrag = 0f

                        val startDate = newCenterDate.minusDays(3)
                        val endDate = newCenterDate.plusDays(3)
                        onDateRangeChanged?.invoke(startDate, endDate)
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title (only recomposes if isCurrentWeek changes)
        CalendarTitle(isCurrentWeek = isCurrentWeek)

        // Names of days
        DayNamesRow(daysInfo = daysInfo)

        Spacer(modifier = Modifier.height(8.dp))

        // Circles of days
        DayCirclesRow(daysInfo = daysInfo)

        val visibleStartDate = daysInfo.first().date
        val visibleEndDate = daysInfo.last().date
        DateRangeLabel(startDate = visibleStartDate, endDate = visibleEndDate)
    }
}

@Composable
private fun CalendarTitle(isCurrentWeek: Boolean) {
    Text(
        text = if (isCurrentWeek) "Hoy" else "Calendario",
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

/**
 * Displays the names of the days in a row.
 * The names are derived from the provided list of DayInfo objects.
 */
@Composable
private fun DayNamesRow(daysInfo: List<DayInfo>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysInfo.forEach { dayInfo ->
            Text(
                text = dayInfo.dayName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(40.dp)
            )
        }
    }
}

@Composable
private fun DayCirclesRow(daysInfo: List<DayInfo>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysInfo.forEach { dayInfo ->
            DayCircle(dayInfo = dayInfo)
        }
    }
}

/**
 * Displays the date range label based on the start and end dates.
 * The label is formatted in Spanish and adjusts based on the month and year.
 */
@Composable
private fun DateRangeLabel(startDate: LocalDate, endDate: LocalDate) {
    val startMonth = startDate.month.getDisplayName(TextStyle.FULL, Locale("es"))
    val endMonth = endDate.month.getDisplayName(TextStyle.FULL, Locale("es"))
    val startYear = startDate.year
    val endYear = endDate.year

    val label = when {
        startMonth == endMonth && startYear == endYear -> {
            "$startMonth $startYear"
        }

        startYear == endYear -> {
            "$startMonth – $endMonth $startYear"
        }

        else -> {
            "$startMonth $startYear – $endMonth $endYear"
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = label.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es")) else it.toString() },
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}


/**
 * Optimized DayCircle using cached DayInfo and memoized styles
 */
@Composable
private fun DayCircle(dayInfo: DayInfo) {
    // Memorize styles to avoid recalculations
    val dayStyle = remember(dayInfo.isToday, dayInfo.isPast, dayInfo.status) {
        calculateDayStyle(dayInfo)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(dayStyle.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        DayContent(
            dayInfo = dayInfo,
            dayStyle = dayStyle
        )
    }
}

@Composable
private fun DayContent(dayInfo: DayInfo, dayStyle: DayStyle) {
    // Always display the day number
    Text(
        text = dayInfo.dayNumber.toString(),
        color = dayStyle.contentColor,
        fontWeight = if (dayInfo.isToday) FontWeight.Bold else FontWeight.Normal,
        fontSize = if (dayInfo.isToday) 16.sp else 14.sp
    )
}

// Pure function for calculating styles (does not recompose)
private fun calculateDayStyle(dayInfo: DayInfo): DayStyle {
    Log.e("DayInfo", "$dayInfo")
    return when {
        dayInfo.isToday -> DayStyle(
            icon = null,
            backgroundColor = when (dayInfo.status) {
                is DayStatus.Completed -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                is DayStatus.Partial -> Color(0xFFFF9800).copy(alpha = 0.2f)
                is DayStatus.Failed -> Color(0xFFF44336).copy(alpha = 0.2f)
                is DayStatus.NoHabits -> Color(0xFF9E9E9E).copy(alpha = 0.1f)
                else -> Color.Transparent
            },
            contentColor = Color(0xFF1976D2) // Material Blue 700
        )

        dayInfo.isPast -> DayStyle(
            icon = null,
            backgroundColor = when (dayInfo.status) {
                is DayStatus.Completed -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                is DayStatus.Partial -> Color(0xFFFF9800).copy(alpha = 0.2f)
                is DayStatus.Failed -> Color(0xFFF44336).copy(alpha = 0.2f)
                is DayStatus.NoHabits -> Color(0xFF9E9E9E).copy(alpha = 0.1f)
                else -> Color.Transparent
            },
            contentColor = when (dayInfo.status) {
                is DayStatus.Completed -> Color(0xFF4CAF50)
                is DayStatus.Partial -> Color(0xFFFF9800)
                is DayStatus.Failed -> Color(0xFFF44336)
                is DayStatus.NoHabits -> Color(0xFF9E9E9E).copy(alpha = 0.6f)
                else -> Color(0xFF757575)
            }
        )

        else -> DayStyle(
            icon = null,
            backgroundColor = Color(0xFF9E9E9E).copy(alpha = 0.2f),
            contentColor = Color(0xFF757575) // Material Grey 600
        )
    }
}