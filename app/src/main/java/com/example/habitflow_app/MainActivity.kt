package com.example.habitflow_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.core.di.dataStore
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.core.utils.JwtUtils
import com.example.habitflow_app.features.authentication.data.datasources.LocalDataStore
import com.example.habitflow_app.navigation.AppNavGraph
import com.example.habitflow_app.navigation.NavDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitflowAppTheme {
                val navController = rememberNavController()
                val localDataStore = remember { LocalDataStore(dataStore) }

                // Check for existing token
                LaunchedEffect(Unit) {
                    val token = localDataStore.getAccessTokenOnce()
                    Log.d("AuthDebug", "Token al iniciar: ${token?.take(5)}...")

                    if (token != null && !JwtUtils.isTokenExpired(token)) {
                        Log.d("AuthDebug", "Token válido encontrado")

                        navController.navigate(NavDestinations.MAIN) {
                            popUpTo(NavDestinations.LOGIN) { inclusive = true }
                        }

                    } else {
                        Log.d("AuthDebug", "Token expirado o no encontrado")
                        localDataStore.clearAccessToken()
                    }
                }

                AppNavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}