package com.example.habitflow_app.features.habits.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.features.articles.ui.components.ArticleCard
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import com.example.habitflow_app.features.habits.ui.components.Calendar
import com.example.habitflow_app.features.habits.ui.components.HabitItem
import com.example.habitflow_app.features.habits.ui.viewmodel.CalendarViewModelImpl

/**
 * HomeScreen is the main screen of the application.
 * Displays a calendar, featured articles, and today's habits.
 *
 * @param modifier An optional modifier to customize the layout.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        // Calendar
        CalendarSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Featured articles
        ArticlesSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Today's habits
        HabitsSection()
    }
}

/**
 * Section that displays the calendar with habit status by day.
 */
@Composable
fun CalendarSection(
    viewModel: CalendarViewModelImpl = hiltViewModel()
) {
    val calendarData by viewModel.calendarData.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    // Load initial data
    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    Log.e("Calendar data: ", "$calendarData")

    Calendar(
        currentDate = currentDate,
        dayStatuses = calendarData,
        onDateRangeChanged = { startDate, endDate ->
            viewModel.onDateRangeChanged(startDate, endDate)
        },
        modifier = Modifier.fillMaxWidth()
    )

}

/**
 * Section that displays featured articles in a horizontal carousel.
 */
@Composable
private fun ArticlesSection(
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val articles by viewModel.rankedArticles.collectAsState()
    val isLoading by viewModel.rankedIsLoading.collectAsState()
    val error by viewModel.rankedError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRankedArticles()
    }

    Column {
        Text(
            text = "Artículos destacados",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            isLoading -> {
                Text("Cargando artículos...")
            }
            error != null -> {
                Text("Error: $error")
            }
            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(articles) { article ->
                        ArticleCard(
                            authorName = article.authorName,
                            authorImageUrl = article.authorImageUrl,
                            title = article.title,
                            description = article.content,
                            modifier = Modifier.width(260.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Section that displays habits scheduled for today.
 */
@Composable
private fun HabitsSection() {
    Column {
        Text(
            text = "Hábitos de hoy",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Example of today's habits
        val habits = getSampleHabits()
        habits.forEach { habit ->
            HabitItem(
                name = habit.name,
                days = habit.days,
                streak = habit.streak,
                isChecked = habit.isCompleted,
                onCheckedChange = { /* Update habit completion status */ },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

// Data classes for examples
data class ArticleData(
    val authorName: String,
    val authorImageUrl: String?,
    val title: String,
    val description: String
)

data class HabitData(
    val name: String,
    val days: String,
    val streak: Int,
    val isCompleted: Boolean
)

// Sample data for preview
private fun getSampleArticles(): List<ArticleData> {
    return listOf(
        ArticleData(
            authorName = "John Cooper",
            authorImageUrl = null,
            title = "Como crear una rutina matutina",
            description = "Empieza el día bien con estas técnicas probadas..."
        ),
        ArticleData(
            authorName = "Sarah Wilson",
            authorImageUrl = null,
            title = "Mindfulness diario",
            description = "Técnicas sencillas de meditación para reducir el estrés..."
        ),
        ArticleData(
            authorName = "Miguel Rodríguez",
            authorImageUrl = null,
            title = "Hábitos para dormir mejor",
            description = "Mejora la calidad de tu sueño con estos consejos..."
        )
    )
}

private fun getSampleHabits(): List<HabitData> {
    return listOf(
        HabitData(
            name = "Meditación",
            days = "Todos los días",
            streak = 5,
            isCompleted = true
        ),
        HabitData(
            name = "Ejercicio",
            days = "Lu, Mi, Vi",
            streak = 3,
            isCompleted = true
        ),
        HabitData(
            name = "Beber 2L de agua",
            days = "Todos los días",
            streak = 9,
            isCompleted = false
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HabitflowAppTheme {
        HomeScreen()
    }
}

