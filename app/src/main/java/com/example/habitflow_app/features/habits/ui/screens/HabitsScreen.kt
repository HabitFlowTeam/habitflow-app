package com.example.habitflow_app.features.habits.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.habits.ui.components.DailyProgressBar
import com.example.habitflow_app.features.habits.ui.components.FullScreenLoadingState
import com.example.habitflow_app.features.habits.ui.components.HabitsSection
import com.example.habitflow_app.features.habits.ui.extensions.getCurrentDayName
import com.example.habitflow_app.features.habits.ui.extensions.getOtherHabits
import com.example.habitflow_app.features.habits.ui.extensions.getTodayHabits
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitUiModel
import com.example.habitflow_app.features.habits.ui.viewmodel.ListHabitsViewModel
import com.example.habitflow_app.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    navController: NavController,
    habitsViewModel: ListHabitsViewModel = hiltViewModel(),
    onHabitClick: (String) -> Unit = { _ -> }
) {
    val uiState by habitsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        habitsViewModel.loadHabits()
    }

    when {
        uiState.isLoading -> {
            FullScreenLoadingState()
        }

        else -> {
            HabitsContent(
                navController = navController,
                habits = uiState.habits,
                onHabitClick = onHabitClick
            )
        }
    }
}

@Composable
private fun HabitsContent(
    navController: NavController,
    habits: List<HabitUiModel>,
    onHabitClick: (String) -> Unit
) {
    // Use extension functions for filtering habits
    val todayHabits = habits.getTodayHabits()
    val otherHabits = habits.getOtherHabits()

    val completedHabits = todayHabits.count { it.isChecked }
    val totalHabits = todayHabits.size
    val hasAnyHabits = habits.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
    ) {
        // Screen title
        Text(
            text = "Mis Hábitos",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Scrollable list of components
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress bar showing today's habit completion (only when we have today's habits)
            if (todayHabits.isNotEmpty()) {
                item {
                    DailyProgressBar(
                        completedHabits = completedHabits,
                        totalHabits = totalHabits,
                        dayOfWeek = getCurrentDayName(),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // If there are no habits display empty state centered in screen
            if (!hasAnyHabits) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyHabitsState(
                            onNewHabitClick = {
                                navController.navigate(NavDestinations.CREATE_HABIT)
                            }
                        )
                    }
                }
            } else {
                // Section for today's habits (checkable) using the reusable component
                item {
                    HabitsSection(
                        title = "Para Hoy",
                        habits = todayHabits,
                        onHabitClick = onHabitClick,
                        isCheckable = true,
                        rightContent = {
                            NewHabitButton(
                                onClick = { navController.navigate(NavDestinations.CREATE_HABIT) }
                            )
                        }
                    )
                }

                // Section for habits not scheduled for today (not checkable)
                if (otherHabits.isNotEmpty()) {
                    item {
                        HabitsSection(
                            title = "Otros Hábitos",
                            habits = otherHabits,
                            onHabitClick = onHabitClick,
                            isCheckable = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewHabitButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Añadir hábito",
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Nuevo hábito")
    }
}

@Composable
fun EmptyHabitsState(onNewHabitClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "No hay hábitos",
            modifier = Modifier
                .size(72.dp)
                .padding(bottom = 16.dp),
            tint = Color.Gray
        )

        Text(
            text = "No hay hábitos creados todavía",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = Color.DarkGray
        )

        Text(
            text = "Empieza creando tu primer hábito para rastrear tu progreso diario",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )

        Button(
            onClick = onNewHabitClick,
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir hábito",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Crear mi primer hábito")
        }
    }
}