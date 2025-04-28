package com.example.habitflow_app.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habitflow_app.core.ui.components.TopAppBar
import com.example.habitflow_app.features.articles.ui.screens.ArticlesMainScreen
import com.example.habitflow_app.features.authentication.ui.screens.LoginScreen
import com.example.habitflow_app.features.gamification.ui.screens.StatsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HabitsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HomeScreen
import com.example.habitflow_app.features.profile.ui.screens.ProfileScreen
import com.example.habitflow_app.navigation.routes.LoginRoute
import com.example.habitflow_app.navigation.routes.RegisterRoute
import com.example.habitflow_app.navigation.ui.screens.LayoutScreen

/**
 * Main navigation graph of the application.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = NavDestinations.LOGIN,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Scaffold(
        topBar = {
            val currentDestination = navController.currentDestination?.route
            if (currentDestination != NavDestinations.LOGIN && currentDestination != NavDestinations.REGISTER) {
                TopAppBar(
                    navController = navController,
                    streakCount = 2,
                    onNotificationsClick = {},
                    onSettingsClick = {},
                    onProfileClick = {}
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
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

            composable(NavDestinations.PROFILE){
                ProfileScreen(
                    navController = navController
                )
            }
        }
    }
}