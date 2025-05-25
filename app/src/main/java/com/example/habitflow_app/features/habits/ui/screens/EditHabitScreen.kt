package com.example.habitflow_app.features.habits.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.habitflow_app.features.habits.ui.viewmodel.DeleteHabitViewModel
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitEditEvent
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    navController: NavController,
    habitId: String,
    editViewModel: HabitEditViewModel = hiltViewModel(),
    deleteViewModel: DeleteHabitViewModel = hiltViewModel()
) {
    val editState by editViewModel.uiState.collectAsState()
    val deleteState by deleteViewModel.deleteState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        editViewModel.loadInitialData(habitId)
    }

    LaunchedEffect(editState.isSuccess) {
        if (editState.isSuccess) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is DeleteHabitViewModel.DeleteState.Success -> {
                navController.popBackStack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Editar Hábito",
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
        if (editState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.LightGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FrequencySelector(
                            isDailySelected = editState.isDailySelected,
                            onFrequencySelected = { isDaily ->
                                editViewModel.onEvent(HabitEditEvent.FrequencyChanged(isDaily))
                            }
                        )

                        if (!editState.isDailySelected) {
                            DaysSelector(
                                selectedDays = editState.selectedDays,
                                onDaySelected = { dayId, isSelected ->
                                    val newDays = if (isSelected) {
                                        editState.selectedDays + dayId
                                    } else {
                                        editState.selectedDays - dayId
                                    }
                                    editViewModel.onEvent(HabitEditEvent.DaysChanged(newDays))
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                editState.daysError?.let {
                    Text(it, color = Color.Red, fontSize = 14.sp)
                }

                editState.error?.let {
                    Text(it, color = Color.Red, fontSize = 14.sp)
                }

                when (deleteState) {
                    is DeleteHabitViewModel.DeleteState.Error -> {
                        Text(
                            (deleteState as DeleteHabitViewModel.DeleteState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                    else -> {}
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    HabitEditActionButtons(
                        onSave = { editViewModel.onEvent(HabitEditEvent.Submit) },
                        onDelete = { showDeleteDialog = true }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar hábito") },
                text = { Text("¿Estás seguro de que quieres eliminar este hábito?") },
                confirmButton = {
                    if (deleteState is DeleteHabitViewModel.DeleteState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        TextButton(
                            onClick = {
                                deleteViewModel.deleteHabit(habitId)
                            }
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false },
                        enabled = deleteState !is DeleteHabitViewModel.DeleteState.Loading
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}