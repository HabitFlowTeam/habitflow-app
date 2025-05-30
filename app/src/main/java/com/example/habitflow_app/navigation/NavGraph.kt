package com.example.habitflow_app.navigation

import android.R.attr.type
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.habitflow_app.core.ui.components.TopAppBar
import com.example.habitflow_app.features.profile.ui.screens.ProfileScreen
import com.example.habitflow_app.navigation.routes.CreateHabitRoute
import com.example.habitflow_app.navigation.routes.EditHabitRoute
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
    modifier: Modifier,
    startDestination: String = NavDestinations.LOGIN
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold()
    { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (currentDestination != null && currentDestination != NavDestinations.LOGIN && currentDestination != NavDestinations.REGISTER && currentDestination != NavDestinations.FORGOT_PASSWORD) {
                TopAppBar(
                    navController = navController,
                    streakCount = 2,
                    onNotificationsClick = {},
                    onSettingsClick = {},
                    onProfileClick = {}
                )
            }

            NavHost(
                navController = navController,
                startDestination = startDestination
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

                composable(NavDestinations.CREATE_HABIT) {
                    CreateHabitRoute(navController = navController)
                }

                composable(
                    route = NavDestinations.EDIT_HABIT,
                    arguments = listOf(navArgument("habitId") { type = NavType.StringType })
                ) { backStackEntry ->
                    EditHabitRoute(
                        navController = navController,
                        habitId = backStackEntry.arguments?.getString("habitId") ?: ""
                    )
                }
            }
        }
    }
}