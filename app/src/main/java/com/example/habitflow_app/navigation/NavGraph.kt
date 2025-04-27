package com.example.habitflow_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habitflow_app.features.articles.ui.screens.ArticlesMainScreen
import com.example.habitflow_app.features.authentication.ui.screens.LoginScreen
import com.example.habitflow_app.features.gamification.ui.screens.StatsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HabitsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HomeScreen
import com.example.habitflow_app.navigation.routes.LoginRoute
import com.example.habitflow_app.navigation.routes.RegisterRoute
import com.example.habitflow_app.navigation.ui.screens.LayoutScreen

/**
 * Main navigation graph of the application.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.MAIN,
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
            LoginRoute(
                navController = navController
            )
        }

        composable(NavDestinations.MAIN) {
            LayoutScreen(
                navController = navController
            )
        }
    }
}