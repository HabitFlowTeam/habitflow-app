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
fun CreateHabitScreen(
    navController: NavController,
    viewModel: HabitsViewModel
) {
    var habitName by remember { mutableStateOf("") }
    var isDailySelected by remember { mutableStateOf(true) }
    var selectedDays by remember { mutableStateOf<List<String>>(emptyList()) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var reminderTime by remember { mutableStateOf<LocalTime?>(LocalTime.of(8, 0)) }

    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()

    var isSubmitting by remember { mutableStateOf(false) }

    val categoryId = "default_category"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Hábito", fontWeight = FontWeight.Normal) },
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

                            viewModel.createHabit(
                                name = habitName,
                                categoryId = categoryId,
                                selectedDays = daysToSave,
                                reminderTime = if (notificationsEnabled) reminderTime else null
                            )

                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}