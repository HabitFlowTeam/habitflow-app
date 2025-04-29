package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.habitflow_app.features.authentication.ui.screens.RegisterScreen
import com.example.habitflow_app.features.authentication.ui.viewmodel.RegisterViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Composable route for registration screen.
 * Wraps the RegisterScreen with navigation and ViewModel dependencies.
 *
 * @param navController Navigation controller for screen transitions
 * @param viewModel ViewModel instance (automatically provided by Hilt)
 */
@Composable
fun RegisterRoute(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreen(
        navController = navController,
        viewModel = viewModel
    )
}
