package com.example.habitflow_app.features.articles.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryChip(
    category: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = category,
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

