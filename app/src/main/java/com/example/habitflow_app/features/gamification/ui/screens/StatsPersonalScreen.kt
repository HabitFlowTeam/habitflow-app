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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsPersonalViewModel

/**
 * Screen that displays the user's personal statistics and achievements.
 * Shows current streak, best streak, completed habits count and motivational message.
 */
@Composable
fun StatsPersonalScreen(
    viewModel: StatsPersonalViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentStreak by viewModel.currentStreak.collectAsState()
    val bestStreak by viewModel.bestStreak.collectAsState()
    val completedHabits by viewModel.completedHabits.collectAsState()
    val weeklyCompletion by viewModel.weeklyCompletion.collectAsState()
    val activeDaysThisMonth by viewModel.activeDaysThisMonth.collectAsState()
    val mostFrequentHabit by viewModel.mostFrequentHabit.collectAsState()
    val barChartData by viewModel.barChartData.collectAsState()
    val pieChartData by viewModel.pieChartData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
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

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando estadísticas...")
            }
            return
        }
        if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $error")
            }
            return
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = currentStreak, label = "Racha actual", modifier = Modifier.weight(1f))
                StatItem(value = bestStreak, label = "Mejor racha", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = completedHabits, label = "Hábitos completados", modifier = Modifier.weight(1f))
                StatItem(value = weeklyCompletion, label = "% Cumplimiento semanal", modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                StatItem(value = activeDaysThisMonth, label = "Días activos este mes", modifier = Modifier.weight(1f))
                //StatItem(value = mostFrequentHabit.ifBlank { 0 }, label = "Hábito más frecuente", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            BarChart(
                data = barChartData,
                labels = listOf("L", "M", "X", "J", "V", "S", "D"),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PieChart(
                data = pieChartData.values.map { it.toFloat() },
                colors = listOf(
                    Color(0xFF4CAF50), // Verde
                    Color(0xFF2196F3), // Azul
                    Color(0xFFFFC107), // Amarillo
                    Color(0xFFF44336), // Rojo
                    Color(0xFF9C27B0), // Extra color si hay más categorías
                    Color(0xFF009688)
                ),
                labels = pieChartData.keys.toList(),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

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
