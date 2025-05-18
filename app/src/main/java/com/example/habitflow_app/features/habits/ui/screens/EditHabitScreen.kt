package com.example.habitflow_app.features.habits.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.habitflow_app.features.habits.data.dto.HabitDayCreateRequest
import com.example.habitflow_app.features.habits.ui.components.*
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitsViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    navController: NavController,
    viewModel: HabitsViewModel,
    habitId: String
) {
    val habits = viewModel.habitsState.collectAsState()
    val habit = habits.value.find { it.id == habitId }

    if (habit == null) {
        LaunchedEffect(key1 = Unit) {
            navController.popBackStack()
        }
        return
    }

    var habitName by remember { mutableStateOf(habit.name) }

    val allDays = listOf("1", "2", "3", "4", "5", "6", "7")
    var selectedDays by remember { mutableStateOf<List<String>>(allDays) }
    var isDailySelected by remember { mutableStateOf(selectedDays.size == 7) }

    var notificationsEnabled by remember { mutableStateOf(habit.notificationsEnabled) }
    var reminderTime by remember { mutableStateOf<LocalTime?>(habit.reminderTime) }

    var isSubmitting by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habitName, fontWeight = FontWeight.Normal) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HabitNameField(
                    name = habitName,
                    onNameChange = { habitName = it }
                )

                FrequencySelector(
                    isDailySelected = isDailySelected,
                    onFrequencySelected = { isDailySelected = it }
                )

                if (!isDailySelected) {
                    DaysSelector(
                        selectedDays = selectedDays,
                        onDaySelected = { dayId, isSelected ->
                            selectedDays = if (isSelected) {
                                selectedDays + dayId
                            } else {
                                selectedDays - dayId
                            }
                        }
                    )
                }

                Divider()

                ReminderSection(
                    isEnabled = notificationsEnabled,
                    onEnabledChange = { notificationsEnabled = it },
                    reminderTime = reminderTime,
                    onTimeChange = { reminderTime = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                if (error.value != null) {
                    Text(
                        text = error.value ?: "",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                HabitActionButtons(
                    onSave = {
                        if (!isSubmitting && habitName.isNotBlank()) {
                            isSubmitting = true

                            val daysToSave = if (isDailySelected) {
                                listOf("1", "2", "3", "4", "5", "6", "7")
                            } else {
                                selectedDays
                            }

                            val daysPairs = daysToSave.map { dayId ->
                                null to dayId
                            }

                            viewModel.updateHabit(
                                habitId = habitId,
                                name = habitName,
                                selectedDays = daysPairs,
                                reminderTime = if (notificationsEnabled) reminderTime else null,
                                notificationsEnabled = notificationsEnabled
                            )

                            navController.popBackStack()
                        }
                    },
                    onDelete = {
                        showDeleteDialog = true
                    }
                )
            }

            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    onConfirm = {
                        viewModel.softDeleteHabit(habitId)
                        showDeleteDialog = false
                        navController.popBackStack()
                    },
                    onDismiss = {
                        showDeleteDialog = false
                    }
                )
            }
        }
    }
}