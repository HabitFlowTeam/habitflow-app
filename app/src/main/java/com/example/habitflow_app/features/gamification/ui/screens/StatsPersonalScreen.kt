package com.example.habitflow_app.features.gamification.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.core.ui.theme.Background
import com.example.habitflow_app.features.gamification.ui.components.BarChart
import com.example.habitflow_app.features.gamification.ui.components.PieChart
import com.example.habitflow_app.features.gamification.ui.components.StatItem

/**
 * Screen that displays the user's personal statistics and achievements.
 * Shows current streak, best streak, completed habits count and motivational message.
 */
@Composable
fun StatsPersonalScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Mis Estadísticas",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Estadísticas personales
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Estadísticas en grid atractivo
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = 15, label = "Racha actual", modifier = Modifier.weight(1f))
                StatItem(value = 30, label = "Mejor racha", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = 120, label = "Hábitos completados", modifier = Modifier.weight(1f))
                StatItem(value = 85, label = "% Cumplimiento semanal", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = 7, label = "Días activos este mes", modifier = Modifier.weight(1f))
                StatItem(value = 3, label = "Hábito más frecuente: Meditar", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Gráfica de barras: hábitos completados por día
            BarChart(
                data = listOf(3, 5, 2, 4, 6, 1, 4),
                labels = listOf("L", "M", "X", "J", "V", "S", "D"),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Gráfica de pastel: distribución de tipos de hábitos
            PieChart(
                data = listOf(40f, 30f, 20f, 10f),
                colors = listOf(
                    Color(0xFF4CAF50), // Verde
                    Color(0xFF2196F3), // Azul
                    Color(0xFFFFC107), // Amarillo
                    Color(0xFFF44336)  // Rojo
                ),
                labels = listOf("Salud", "Productividad", "Bienestar", "Otros"),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Mensaje motivacional
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Sigue así! Estás construyendo hábitos saludables día a día.",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            }
        }
    }
}
