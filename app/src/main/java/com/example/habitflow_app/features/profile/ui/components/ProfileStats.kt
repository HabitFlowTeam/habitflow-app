package com.example.habitflow_app.features.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.features.gamification.ui.components.StatItem

/**
 * A composable function that displays a row of profile statistics.
 *
 * @param currentStreak The current streak of consecutive days.
 * @param bestStreak The best streak of consecutive days.
 * @param completedHabits The total number of completed habits.
 * @param modifier An optional [Modifier] to customize the layout of the component.
 */
@Composable
fun ProfileStats(
    currentStreak: Int,
    bestStreak: Int,
    completedHabits: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Current streak
        StatItem(
            value = currentStreak,
            label = "Racha actual",
            modifier = Modifier.weight(1f)
        )

        // Best streak
        StatItem(
            value = bestStreak,
            label = "Mejor racha",
            modifier = Modifier.weight(1f)
        )

        // Completed habits
        StatItem(
            value = completedHabits,
            label = "Completados",
            modifier = Modifier.weight(1f)
        )
    }
}