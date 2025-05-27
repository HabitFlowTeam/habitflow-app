package com.example.habitflow_app.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Black
import com.example.habitflow_app.core.ui.theme.White
import com.example.habitflow_app.core.ui.theme.Zinc100

/**
 * Displays a horizontally scrollable list of category chips for filtering content.
 * Adds a subtle fade effect on the right edge when scrollable.
 *
 * @param categories A list of category labels to be displayed.
 * @param selectedCategory The currently selected category.
 * @param onCategorySelected Callback triggered when a category is selected.
 */
@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp)
            .fadeRightEdge()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.forEach { category ->
                CategoryChip(
                    label = category,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

/**
 * A single selectable chip representing a category.
 * Highlights the chip if selected and calls the provided callback when clicked.
 *
 * @param label The text to display inside the chip.
 * @param isSelected Whether the chip is currently selected.
 * @param onClick Callback triggered when the chip is clicked.
 */
@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Black else Zinc100
    val contentColor = if (isSelected) White else Black

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = AppTypography.bodyMedium,
            color = contentColor
        )
    }
}

/**
 * Adds a horizontal gradient to the right edge of the content to simulate a fade-out effect.
 * Useful for scrollable containers to subtly indicate overflowing content.
 *
 * @return The [Modifier] with the applied right-edge fade effect.
 */
fun Modifier.fadeRightEdge(): Modifier = this.then(
    Modifier.drawWithContent {
        drawContent()
        val fadeWidth = 48.dp.toPx()
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, Color.White),
                startX = size.width - fadeWidth,
                endX = size.width
            ),
            size = size
        )
    }
)

@Preview(showBackground = true)
@Composable
fun CategoryFilterPreview() {
    var selected by remember { mutableStateOf("Todos") }
    val categories = listOf(
        "Todos", "Productividad", "Ejercicio", "Meditación",
        "Lectura", "Sueño", "Salud", "Estudio", "Trabajo", "Relax"
    )

    CategoryFilterRow(
        categories = categories,
        selectedCategory = selected,
        onCategorySelected = { selected = it }
    )
}
