package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.authentication.ui.screens.LoginScreen
import com.example.habitflow_app.features.authentication.ui.viewmodel.LoginViewModel

/**
 * Composable route for login screen.
 * Wraps the LoginScreen with navigation and ViewModel dependencies.
 *
 * @param navController Navigation controller for screen transitions
 * @param viewModel ViewModel instance (automatically provided by Hilt)
 */
@Composable
fun LoginRoute(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LoginScreen(
        navController = navController,
        viewModel = viewModel
    )
}