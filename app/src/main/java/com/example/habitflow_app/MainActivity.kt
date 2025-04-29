package com.example.habitflow_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataSourceProvider
import com.example.habitflow_app.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "AppVariables"
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalDataSourceProvider.init(applicationContext.dataStore)
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
