package com.example.habitflow_app.features.habits.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp

/**
 * Common loading states used across the app
 */

/**
 * Full screen loading state with circular progress indicator
 */
@Composable
fun FullScreenLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Inline loading text for smaller components
 */
@Composable
fun InlineLoadingText(text: String = "Cargando...") {
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Gray,
        fontStyle = FontStyle.Italic
    )
}