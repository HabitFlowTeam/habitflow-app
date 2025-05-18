package com.example.habitflow_app.features.habits.ui.screens

// Import statements for layout, UI elements, and models
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.domain.models.Habit
import com.example.habitflow_app.domain.models.HabitTracking
import com.example.habitflow_app.features.habits.ui.components.DailyProgressBar
import com.example.habitflow_app.features.habits.ui.components.HabitItem
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    todayHabits: List<Pair<Habit, HabitTracking>> = emptyList(), // Habits to complete today
    otherHabits: List<Pair<Habit, HabitTracking>> = emptyList(), // Habits not scheduled for today
    onHabitCheckedChange: (String, Boolean) -> Unit = { _, _ -> }, // Callback when checkbox state changes
    onNewHabitClick: () -> Unit = {} // Callback for new habit button
) {
    // Count completed habits for today
    val completedHabits = todayHabits.count { it.second.isChecked }
    val totalHabits = todayHabits.size
    val currentDay = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val hasAnyHabits = todayHabits.isNotEmpty() || otherHabits.isNotEmpty()

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
                        EmptyHabitsState(onNewHabitClick)
                    }
                }
            } else {
                // Section for today's habits
                item {
                    HabitsSection(
                        title = "Para Hoy",
                        habits = todayHabits,
                        onHabitCheckedChange = onHabitCheckedChange,
                        rightContent = {
                            Button(
                                onClick = onNewHabitClick,
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
                        })
                }

                // Section for habits not scheduled for today (only when we have other habits)
                if (otherHabits.isNotEmpty()) {
                    item {
                        HabitsSection(
                            title = "Otros Hábitos",
                            habits = otherHabits,
                            onHabitCheckedChange = onHabitCheckedChange
                        )
                    }
                }
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

@Composable
fun HabitsSection(
    title: String, // Section title
    habits: List<Pair<Habit, HabitTracking>>, // Habits list
    onHabitCheckedChange: (String, Boolean) -> Unit, // Checkbox callback
    rightContent: @Composable (() -> Unit)? = null // Optional trailing content (e.g. button)
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Section title and optional right-aligned content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold
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
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        } else {
            habits.forEach { (habit, tracking) ->
                val daysText = getHabitDaysText(habit.id)

                HabitItem(
                    name = habit.name,
                    days = daysText,
                    streak = habit.streak,
                    isChecked = tracking.isChecked,
                    onCheckedChange = { checked ->
                        onHabitCheckedChange(habit.id, checked)
                    })
            }
        }
    }
}

// Simulates fetching a text representation of the days associated with a habit
private fun getHabitDaysText(habitId: String): String {
    return when (habitId) {
        "1" -> "Lunes, Miércoles, Viernes"
        "2" -> "Diario"
        "3" -> "Diario"
        "4" -> "Martes, Jueves, Sábado"
        else -> "Diario"
    }
}

// Preview for Compose UI editor with mock data
@Preview(showBackground = true)
@Composable
fun HabitsScreenPreview() {
    MaterialTheme {
        val today = LocalDate.now()

        // Sample habits
        val habits = listOf(
            Habit(
                id = "1",
                name = "Meditación",
                streak = 5,
                notificationsEnabled = true,
                reminderTime = LocalTime.of(8, 0),
                expirationDate = today.plusMonths(3),
                categoryId = "mindfulness",
                userId = "user1"
            ),
            Habit(
                id = "2",
                name = "Ejercicio",
                streak = 12,
                notificationsEnabled = true,
                reminderTime = LocalTime.of(18, 0),
                expirationDate = today.plusMonths(6),
                categoryId = "fitness",
                userId = "user1"
            ),
            Habit(
                id = "3",
                name = "Beber 2L de agua",
                streak = 15,
                notificationsEnabled = false,
                reminderTime = null,
                expirationDate = today.plusMonths(12),
                categoryId = "health",
                userId = "user1"
            ),
            Habit(
                id = "4",
                name = "Yoga",
                streak = 3,
                notificationsEnabled = true,
                reminderTime = LocalTime.of(7, 0),
                expirationDate = today.plusMonths(3),
                categoryId = "fitness",
                userId = "user1"
            )
        )

        // Sample habit tracking data
        val habitTrackings = listOf(
            HabitTracking("t1", isChecked = true, trackingDate = today, habitId = "1"),
            HabitTracking("t2", isChecked = true, trackingDate = today, habitId = "2"),
            HabitTracking("t3", isChecked = false, trackingDate = today, habitId = "3"),
            HabitTracking("t4", isChecked = true, trackingDate = today, habitId = "4")
        )

        // Assign first 3 as today's habits
        val todayHabits = habits.take(3).mapIndexed { index, habit ->
            habit to habitTrackings[index]
        }

        // Assign remaining as other habits
        val otherHabits = listOf(habits[3] to habitTrackings[3])

        HabitsScreen(
            todayHabits = todayHabits,
            otherHabits = otherHabits
        )
    }
}

// Preview for empty state
@Preview(showBackground = true)
@Composable
fun EmptyHabitsScreenPreview() {
    MaterialTheme {
        HabitsScreen(
            todayHabits = emptyList(),
            otherHabits = emptyList()
        )
    }
}