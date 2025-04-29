package com.example.habitflow_app.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habitflow_app.core.ui.components.TopAppBar
import com.example.habitflow_app.features.profile.ui.screens.ProfileScreen
import com.example.habitflow_app.navigation.routes.ForgotPasswordRoute
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
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
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

            composable(NavDestinations.LOGIN) {
                LoginRoute(
                    navController = navController
                )
            }

            composable(NavDestinations.MAIN) {
                LayoutScreen(
                    navController = navController
                )
            }

            composable(NavDestinations.PROFILE) {
                ProfileScreen(
                    navController = navController
                )
            }

            composable(NavDestinations.FORGOT_PASSWORD) {
                ForgotPasswordRoute(navController = navController)
            }
        }
    }
}