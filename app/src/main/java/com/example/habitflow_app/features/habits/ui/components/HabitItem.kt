package com.example.habitflow_app.features.habits.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.R
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Black
import com.example.habitflow_app.core.ui.theme.Green200
import com.example.habitflow_app.core.ui.theme.Zinc400
import com.example.habitflow_app.core.ui.theme.Zinc500


/**
 * A preview of the HabitItem composable
 */
@Preview(showBackground = true)
@Composable
fun HabitItemPreview() {
        HabitItem(
            name = "Meditación",
            days = "Lunes, Jueves, Viernes",
            streak = 5,
            isChecked = false,
            onCheckedChange = {}
        )
}

/**
 * A composable function that represents a habit item in the user interface.
 *
 * @param name The name of the habit.
 * @param days The days on which the habit is performed (formatted as a string).
 * @param streak The current streak of consecutive days the habit has been completed.
 * @param isChecked Indicates whether the habit is marked as completed.
 * @param onCheckedChange A callback triggered when the checkbox state changes.
 * @param modifier An optional [Modifier] to customize the layout of the component.
 */
@Composable
fun HabitItem(
    name: String,
    days: String,
    streak: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Zinc400,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Título del hábito
            Text(
                text = name,
                style = AppTypography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Días del hábito
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_schedule),
                    contentDescription = "Días de hábito",
                    modifier = Modifier.size(16.dp),
                    tint = Zinc500
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = days,
                    style = AppTypography.bodyMedium,
                    color = Zinc500
                )
            }

            // Racha actual
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_streak),
                    contentDescription = "Racha actual",
                    modifier = Modifier.size(16.dp),
                    tint = Zinc500
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Racha: $streak días",
                    style = AppTypography.bodyMedium,
                    color = Zinc500
                )
            }
        }

        // Checkbox
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Green200,
                uncheckedColor = Zinc400
            )
        )
    }
}