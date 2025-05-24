package com.example.habitflow_app.navigation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.R
import com.example.habitflow_app.core.ui.components.BottomNavigationBar
import com.example.habitflow_app.features.articles.ui.screens.ArticlesMainScreen
import com.example.habitflow_app.features.gamification.ui.screens.StatsScreen
import com.example.habitflow_app.features.habits.ui.screens.HabitsScreen
import com.example.habitflow_app.features.habits.ui.screens.HomeScreen
import com.example.habitflow_app.navigation.NavDestinations
import com.example.habitflow_app.navigation.ui.components.BottomNavItem

/**
 * A composable function that represents the main layout of the application.
 * It includes a top app bar, a bottom navigation bar, and a navigation host
 * for managing different screens within the app.
 *
 * @param navController The navigation controller used to manage navigation between screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutScreen(
    navController: NavController = rememberNavController()
) {
    val barNavController = rememberNavController()
    val bottomNavItem = listOf(
        BottomNavItem(NavDestinations.HOME, R.drawable.ic_home, "Inicio"),
        BottomNavItem(NavDestinations.HABITS, R.drawable.ic_habits, "Hábitos"),
        BottomNavItem(NavDestinations.GAMIFICATION, R.drawable.ic_stats, "Estadísticas"),
        BottomNavItem(NavDestinations.ARTICLES, R.drawable.ic_social, "Artículos"),
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            NavHost(
                navController = barNavController,
                startDestination = NavDestinations.HOME,
            ) {
                composable(NavDestinations.HOME) { HomeScreen() }
                composable(NavDestinations.ARTICLES) { ArticlesMainScreen() }
                composable(NavDestinations.HABITS) { HabitsScreen() }
                composable(NavDestinations.GAMIFICATION) { StatsScreen() }
            }
        }
        BottomNavigationBar(
            items = bottomNavItem,
            navController = barNavController
        )

    }

}
