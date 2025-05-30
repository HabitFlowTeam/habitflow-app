package com.example.habitflow_app.features.profile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Background
import com.example.habitflow_app.core.ui.theme.Black


/**
 * A composable function that displays a statistic item with a value and a label.
 *
 * @param value The numerical value of the statistic.
 * @param label The label describing the statistic.
 * @param modifier An optional [Modifier] to customize the layout of the component.
 */
@Composable
fun StatItem(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // numerical value
        Text(
            text = value.toString(),
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        // label
        Text(
            text = label,
            style = AppTypography.bodyMedium,
            color = Black
        )
    }
}