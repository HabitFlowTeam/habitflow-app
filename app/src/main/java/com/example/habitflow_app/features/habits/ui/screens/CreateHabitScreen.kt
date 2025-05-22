package com.example.habitflow_app.features.habits.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.habits.ui.components.*
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitCreationEvent
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitsViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    navController: NavController,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var isDailySelected by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (state.categories.isEmpty() && !state.isLoadingCategories) {
            viewModel.onEvent(HabitCreationEvent.RetryLoadCategories)
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nuevo Hábito",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
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
        if (state.isLoadingCategories) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HabitNameField(
                    name = state.name,
                    onNameChange = { viewModel.onEvent(HabitCreationEvent.NameChanged(it)) }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CategorySelector(
                            categories = state.categories,
                            selectedCategoryId = state.categoryId,
                            onCategorySelected = { viewModel.onEvent(HabitCreationEvent.CategoryChanged(it)) }
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        FrequencySelector(
                            isDailySelected = isDailySelected,
                            onFrequencySelected = { isDailySelected = it }
                        )

                        if (!isDailySelected) {
                            DaysSelector(
                                selectedDays = state.selectedDays,
                                onDaySelected = { dayId, isSelected ->
                                    val newDays = if (isSelected) {
                                        state.selectedDays + dayId
                                    } else {
                                        state.selectedDays - dayId
                                    }
                                    viewModel.onEvent(HabitCreationEvent.DaysChanged(newDays))
                                }
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ReminderSection(
                            isEnabled = state.notificationsEnabled,
                            onEnabledChange = { viewModel.onEvent(HabitCreationEvent.NotificationsToggled(it)) },
                            reminderTime = state.reminderTime,
                            onTimeChange = { viewModel.onEvent(HabitCreationEvent.ReminderTimeChanged(it)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                state.error?.let {
                    Text(it, color = Color.Red, fontSize = 14.sp)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    HabitActionButtons(
                        onSave = { viewModel.onEvent(HabitCreationEvent.Submit) }
                    )
                }
            }
        }
    }
}