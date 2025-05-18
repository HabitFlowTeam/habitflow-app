package com.example.habitflow_app.features.habits.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.core.ui.theme.Zinc400

/**
 * A composable that displays daily habit completion progress with:
 * - Current day indicator
 * - Completion text summary
 * - Visual progress bar with percentage
 *
 * @param completedHabits Number of habits completed today
 * @param totalHabits Total number of habits scheduled for today
 * @param dayOfWeek Current day of week to display
 * @param modifier Optional Modifier for styling/layout
 */
@Composable
fun DailyProgressBar(
    completedHabits: Int,
    totalHabits: Int,
    dayOfWeek: String,
    modifier: Modifier = Modifier
) {
    // Calculate completion percentage (0-100)
    val progressPercentage =
        if (totalHabits > 0) (completedHabits.toFloat() / totalHabits) * 100 else 0f

    // Generate progress description text
    val progressText =
        "Has completado $completedHabits de $totalHabits hábitos programados para hoy"

    // Main container with border and padding matching HabitItem style
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Zinc400,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Content column that takes all available width
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Header row with title and day indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Progress title
                Text(
                    text = "Progreso de Hoy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Day of week indicator pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = dayOfWeek,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress summary text
            Text(
                text = progressText,
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar row (bar + percentage)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Progress bar background track
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.5f))
                ) {
                    // Progress bar fill (colored portion)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressPercentage / 100f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .align(Alignment.CenterStart)
                    )
                }

                // Percentage text display
                Text(
                    text = "${progressPercentage.toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Preview function for DailyProgressBar component
 */
@Preview(showBackground = true)
@Composable
fun DailyProgressBarPreview() {
    Surface(color = Color(0xFFF3F3F3)) {
        DailyProgressBar(
            completedHabits = 1,
            totalHabits = 3,
            dayOfWeek = "Lunes"
        )
    }
}