package com.example.habitflow_app.features.habits.ui.screens

// Import statements for layout, UI elements, and models
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.habits.ui.components.DailyProgressBar
import com.example.habitflow_app.features.habits.ui.components.HabitItem
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitUiModel
import com.example.habitflow_app.features.habits.ui.viewmodel.ListHabitsViewModel
import com.example.habitflow_app.navigation.NavDestinations
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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
            LoadingState()
        }

        else -> {
            HabitsContent(
                navController = navController,
                habits = uiState.habits,
                onHabitCheckedChange = { habitId, isChecked ->
                    habitsViewModel.updateHabitStatus(habitId, isChecked)
                },
                onHabitClick = onHabitClick
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HabitsContent(
    navController: NavController,
    habits: List<HabitUiModel>,
    onHabitCheckedChange: (String, Boolean) -> Unit,
    onHabitClick: (String) -> Unit
) {
    // Separate today's habits from other habits
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val todayHabits = habits.filter { habit ->
        habit.isScheduledForToday()
    }
    val otherHabits = habits.filter { habit ->
        !habit.isScheduledForToday()
    }

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
                        dayOfWeek = currentDay,
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
                // Section for today's habits (checkable)
                item {
                    HabitsSection(
                        title = "Para Hoy",
                        habits = todayHabits,
                        onHabitCheckedChange = onHabitCheckedChange,
                        onHabitClick = onHabitClick,
                        isCheckable = true, // Today's habits CAN be marked
                        rightContent = {
                            Button(
                                onClick = { navController.navigate(NavDestinations.CREATE_HABIT) },
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
                    )
                }

                // Section for habits not scheduled for today (not checkable)
                if (otherHabits.isNotEmpty()) {
                    item {
                        HabitsSection(
                            title = "Otros Hábitos",
                            habits = otherHabits,
                            onHabitCheckedChange = onHabitCheckedChange,
                            onHabitClick = onHabitClick,
                            isCheckable = false // The other habits CANNOT be checked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HabitsSection(
    title: String,
    habits: List<HabitUiModel>,
    onHabitCheckedChange: (String, Boolean) -> Unit,
    onHabitClick: ((String) -> Unit)? = null,
    rightContent: @Composable (() -> Unit)? = null,
    isCheckable: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Section title and optional right-aligned content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (rightContent != null) {
                rightContent()
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Display each habit as a HabitItem
        if (habits.isEmpty()) {
            Text(
                text = "No hay hábitos en esta sección",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        } else {
            habits.forEach { habit ->
                HabitItem(
                    name = habit.name,
                    days = habit.days,
                    streak = habit.streak,
                    isChecked = habit.isChecked,
                    onCheckedChange = { checked ->
                        if (isCheckable) {
                            onHabitCheckedChange(habit.id, checked)
                        }
                    },
                    onClick = { onHabitClick?.invoke(habit.id) },
                    isCheckable = isCheckable
                )
            }
        }
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

// Extension function to determine if a habit is scheduled for today
private fun HabitUiModel.isScheduledForToday(): Boolean {
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return this.days.contains(currentDay, ignoreCase = true) || this.days == "Todos los días"
}