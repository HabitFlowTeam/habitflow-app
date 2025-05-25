package com.example.habitflow_app.features.habits.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitUiModel

/**
 * Reusable component for displaying a section of habits with a title and optional right content
 *
 * @param title The section title
 * @param habits List of habits to display
 * @param onHabitClick Optional callback when a habit is clicked
 * @param rightContent Optional composable content to display on the right side of the title
 * @param isCheckable Whether habits in this section can be checked/unchecked
 * @param emptyStateMessage Message to show when there are no habits
 * @param useHomeStyle Whether to use the home screen styling (different typography)
 */
@Composable
fun HabitsSection(
    title: String,
    habits: List<HabitUiModel>,
    onHabitClick: ((String) -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    isCheckable: Boolean = true,
    emptyStateMessage: String = "No hay hábitos en esta sección",
    useHomeStyle: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Section title and optional right-aligned content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = if (useHomeStyle) {
                    MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    MaterialTheme.typography.titleSmall.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = if (useHomeStyle) Modifier.padding(bottom = 12.dp) else Modifier
            )

            rightContent?.invoke()
        }

        if (!useHomeStyle) {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Display habits or empty state
        if (habits.isEmpty()) {
            Text(
                text = emptyStateMessage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = if (useHomeStyle) 16.dp else 8.dp),
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        } else {
            habits.forEach { habit ->
                HabitItem(
                    name = habit.name,
                    days = habit.days,
                    streak = habit.streak,
                    isChecked = habit.isChecked,
                    onCheckedChange = { checked ->
                        if (isCheckable) {
                            habit.onCheckedChange(checked)
                        }
                    },
                    onClick = { 
                        onHabitClick?.invoke(habit.id) 
                    },
                    isCheckable = isCheckable,
                    modifier = if (useHomeStyle) Modifier.padding(bottom = 12.dp) else Modifier
                )
            }
        }
    }
}