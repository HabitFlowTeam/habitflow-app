package com.example.habitflow_app.navigation.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.core.ui.components.BottomNavigationBar
import com.example.habitflow_app.features.articles.ui.screens.ArticlesMainScreen
import com.example.habitflow_app.features.gamification.ui.screens.StatsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HabitsMainScreen
import com.example.habitflow_app.features.habits.ui.screens.HomeScreen
import com.example.habitflow_app.navigation.NavDestinations
import com.example.habitflow_app.navigation.ui.components.BottomNavItem
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutScreen(
    navController: NavController = rememberNavController()
) {
    val barNavController = rememberNavController()
    val bottomNavItem = listOf(
        BottomNavItem(NavDestinations.HOME, Icons.Default.Home, "Home"),
        BottomNavItem(NavDestinations.ARTICLES, Icons.Default.Star, "Articles"),
        BottomNavItem(NavDestinations.HABITS, Icons.Default.Menu, "Habits"),
        BottomNavItem(NavDestinations.GAMIFICATION, Icons.Default.ThumbUp, "Stats"),
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                ),
                title = {
                    Text(
                        text = "HabitFlow",
                        color = Color.White
                    )
                }
            )
        },

        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItem,
                navController = barNavController
            )
        }
    ) {innerPadding ->
        NavHost(navController = barNavController, startDestination= NavDestinations.HOME, modifier = Modifier.padding(innerPadding)) {
            composable(NavDestinations.HOME) { HomeScreen() }
            composable(NavDestinations.ARTICLES) { ArticlesMainScreen() }
            composable(NavDestinations.HABITS) { HabitsMainScreen() }
            composable(NavDestinations.GAMIFICATION) { StatsMainScreen() }
        }

    }
}