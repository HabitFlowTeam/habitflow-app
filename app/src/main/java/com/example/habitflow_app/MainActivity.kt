package com.example.habitflow_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitflowAppTheme {
                HabitFlowApp()
            }
        }
    }
}

@Composable
fun HabitFlowApp() {
    val navController = rememberNavController()

    AppNavGraph(
        navController = navController,
        modifier = Modifier
            .fillMaxSize()
    )
}
