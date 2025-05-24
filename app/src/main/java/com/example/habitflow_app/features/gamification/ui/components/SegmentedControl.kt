package com.example.habitflow_app.features.gamification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.*

/**
 * A segmented control UI component that displays two tabs.
 * The selected tab is highlighted with a white background and bold text,
 * while the unselected tab has a muted background and lighter text.
 *
 * @param options List of labels to be displayed as tabs (usually two).
 * @param selectedIndex Index of the currently selected tab.
 * @param onTabSelected Callback triggered when a tab is clicked.
 * @param modifier Modifier for styling the control container.
 */
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc100)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            val backgroundColor = if (isSelected) White else Zinc100
            val textColor = if (isSelected) Black else Zinc500

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = AppTypography.bodyMedium,
                    color = textColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedControlPreview() {
    var selected by remember { mutableStateOf(0) }
    val tabs = listOf("Personales", "Globales")

    SegmentedControl(
        options = tabs,
        selectedIndex = selected,
        onTabSelected = { selected = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
