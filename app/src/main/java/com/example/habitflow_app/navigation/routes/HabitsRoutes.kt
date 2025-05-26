package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.habits.ui.screens.CreateHabitScreen
import com.example.habitflow_app.features.habits.ui.screens.EditHabitScreen
import com.example.habitflow_app.features.habits.ui.viewmodel.HabitsViewModel

@Composable
fun CreateHabitRoute(
    navController: NavController,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    CreateHabitScreen(
        navController = navController,
        viewModel = viewModel
    )
}

@Composable
fun EditHabitRoute(
    navController: NavController,
    habitId: String
) {
    EditHabitScreen(
        navController = navController,
        habitId = habitId
    )
}