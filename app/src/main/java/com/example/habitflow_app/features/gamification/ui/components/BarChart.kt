package com.example.habitflow_app.features.gamification.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BarChart(
    data: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary
) {
    val max = (data.maxOrNull() ?: 1).toFloat()
    Column(modifier = modifier) {
        Text(
            text = "Hábitos completados por día",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.height(120.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEachIndexed { i, value ->
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Canvas(modifier = Modifier.height(100.dp).width(20.dp)) {
                        val barHeight = (value / max) * size.height
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(0f, size.height - barHeight),
                            size = androidx.compose.ui.geometry.Size(size.width, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                    }
                    Text(text = labels[i], fontSize = 12.sp)
                }
            }
        }
    }
}
