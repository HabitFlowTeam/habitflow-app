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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.articles.ui.components.ArticleCard
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import com.example.habitflow_app.features.habits.ui.components.Calendar
import com.example.habitflow_app.features.habits.ui.components.HabitsSection
import com.example.habitflow_app.features.habits.ui.components.InlineLoadingText
import com.example.habitflow_app.features.habits.ui.extensions.getTodayHabits
import com.example.habitflow_app.features.habits.ui.viewmodel.CalendarViewModelImpl
import com.example.habitflow_app.features.habits.ui.viewmodel.ListHabitsViewModel
import com.example.habitflow_app.navigation.NavDestinations

/**
 * HomeScreen is the main screen of the application.
 * Displays a calendar, featured articles, and today's habits.
 *
 * @param modifier An optional modifier to customize the layout.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController
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
        ArticlesSection(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        // Today's habits
        TodayHabitsSection()
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
    viewModel: ArticleViewModel = hiltViewModel(),
    navController: NavController
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
                            articleId = article.articleId,

                            onClick = { id ->
                                navController.navigate(NavDestinations.articleDetailRoute(id))
                            },
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
 * Now using the reusable HabitsSection component.
 */
@Composable
private fun TodayHabitsSection(
    habitsViewModel: ListHabitsViewModel = hiltViewModel()
) {
    val uiState by habitsViewModel.uiState.collectAsState()

    // Load habits data
    LaunchedEffect(Unit) {
        habitsViewModel.loadHabits()
    }

    // Filter only today's habits using the extension function
    val todayHabits = uiState.habits.getTodayHabits()

    when {
        uiState.isLoading -> {
            InlineLoadingText("Cargando hábitos...")
        }

        else -> {
            HabitsSection(
                title = "Hábitos de hoy",
                habits = todayHabits,
                onHabitClick = { /* Navigate to habit details */ },
                isCheckable = true,
                emptyStateMessage = "No tienes hábitos programados para hoy",
                useHomeStyle = true
            )
        }
    }
}
