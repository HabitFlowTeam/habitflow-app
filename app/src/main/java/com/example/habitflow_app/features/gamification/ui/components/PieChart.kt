package com.example.habitflow_app.features.gamification.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PieChart(
    data: List<Float>,
    colors: List<Color>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val total = data.sum()
    Column(modifier = modifier, horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text(
            text = "Distribución de hábitos",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Centrar y agrandar el gráfico
        Canvas(modifier = Modifier.size(180.dp)) {
            var startAngle = -90f
            data.forEachIndexed { i, value ->
                val sweep = if (total == 0f) 0f else (value / total) * 360f
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = size
                )
                startAngle += sweep
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Leyenda debajo del gráfico
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            labels.forEachIndexed { i, label ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(12.dp).background(colors.getOrElse(i) { Color.Gray })
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = label, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }
    }
}
