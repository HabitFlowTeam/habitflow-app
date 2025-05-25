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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitflow_app.features.articles.ui.components.ArticleCard
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import com.example.habitflow_app.features.habits.ui.components.Calendar
import com.example.habitflow_app.features.habits.ui.components.HabitItem
import com.example.habitflow_app.features.habits.ui.viewmodel.CalendarViewModelImpl
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitUiModel
import com.example.habitflow_app.features.habits.ui.viewmodel.ListHabitsViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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
 * Now using real data from the ViewModel instead of sample data.
 */
@Composable
private fun HabitsSection(
    habitsViewModel: ListHabitsViewModel = hiltViewModel()
) {
    val uiState by habitsViewModel.uiState.collectAsState()

    // Load habits data
    LaunchedEffect(Unit) {
        habitsViewModel.loadHabits()
    }

    // Filter only today's habits
    val todayHabits = uiState.habits.filter { habit ->
        habit.isScheduledForToday()
    }

    Column {
        Text(
            text = "Hábitos de hoy",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            uiState.isLoading -> {
                Text(
                    text = "Cargando hábitos...",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic
                )
            }

            todayHabits.isEmpty() -> {
                Text(
                    text = "No tienes hábitos programados para hoy",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

            else -> {
                todayHabits.forEach { habit ->
                    HabitItem(
                        name = habit.name,
                        days = habit.days,
                        streak = habit.streak,
                        isChecked = habit.isChecked,
                        onCheckedChange = { checked ->
                            habit.onCheckedChange(checked)
                        },
                        onClick = { /* Navigate to habit details */ },
                        isCheckable = true,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

private fun HabitUiModel.isScheduledForToday(): Boolean {
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return this.days.contains(currentDay, ignoreCase = true) || this.days == "Todos los días"
}
