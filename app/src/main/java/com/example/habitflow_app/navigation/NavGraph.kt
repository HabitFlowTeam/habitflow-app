package com.example.habitflow_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habitflow_app.features.authentication.ui.screens.LoginScreen
import com.example.habitflow_app.features.habits.ui.screens.HomeScreen
import com.example.habitflow_app.navigation.routes.RegisterRoute

/**
 * Main navigation graph of the application.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.REGISTER,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Registry route
        composable(NavDestinations.REGISTER) {
            RegisterRoute(
                navController = navController
            )
        }

        composable(NavDestinations.LOGIN){
            LoginScreen()
        }

        composable(NavDestinations.HOME) {
            HomeScreen()
        }
    }
}