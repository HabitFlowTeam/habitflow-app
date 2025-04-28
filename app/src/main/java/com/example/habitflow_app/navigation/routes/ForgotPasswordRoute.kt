package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.habitflow_app.features.authentication.ui.screens.ForgotPasswordScreen
import com.example.habitflow_app.features.authentication.ui.viewmodel.ForgotPasswordViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ForgotPasswordRoute(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    ForgotPasswordScreen(
        navController = navController,
        viewModel = viewModel
    )
}