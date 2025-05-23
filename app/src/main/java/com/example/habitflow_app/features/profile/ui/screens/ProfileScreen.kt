package com.example.habitflow_app.features.profile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Red500
import com.example.habitflow_app.features.profile.ui.components.MyArticleItem
import com.example.habitflow_app.features.profile.ui.components.ProfileHeader
import com.example.habitflow_app.features.profile.ui.components.ProfileStats
import com.example.habitflow_app.features.profile.ui.viewmodel.ProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitflow_app.core.ui.theme.Background
import com.example.habitflow_app.navigation.NavDestinations
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import kotlin.collections.isNotEmpty
import kotlin.collections.sortedByDescending

/**
 * A composable function that displays the profile screen, including the top app bar,
 * profile header, statistics, and a list of articles.
 */
@Composable
fun ProfileScreen(
    navController: NavController = rememberNavController(),
    viewModel: ProfileViewModel = hiltViewModel(),
    articleViewModel: ArticleViewModel = hiltViewModel()
) {
    val profile by viewModel.profileState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val profileArticles by articleViewModel.profileArticles.collectAsState()
    val articlesLoading by articleViewModel.isLoading.collectAsState()
    val articlesError by articleViewModel.error.collectAsState()

    LaunchedEffect(profile?.id) {
        if (profile?.id != null) {
            articleViewModel.loadUserArticles(profile!!.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.padding(8.dp))

        // Row with back arrow and screen title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrowBack",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Mi perfil",
                style = AppTypography.titleLarge,
            )
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error!!, color = Red500)
                }
            }
            profile != null -> {
                // Profile header with user information
                ProfileHeader(
                    name = profile!!.fullName,
                    username = profile!!.username,
                    imageUrl = profile!!.avatarUrl
                )

                // Profile statistics
                ProfileStats(
                    currentStreak = profile!!.streak,
                    bestStreak = profile!!.bestStreak,
                    completedHabits = 100
                )

                // Logout button
                Button(
                    onClick = {
                        viewModel.logout()
                        navController.navigate(NavDestinations.LOGIN) {
                            popUpTo(0) // Limpia toda la pila de navegación
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red500
                    )
                ) {
                    Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
                }

                // Artículos del usuario
                when {
                    articlesLoading -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    articlesError != null -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(text = articlesError!!, color = Red500)
                        }
                    }
                    profileArticles.isNotEmpty() -> {
                        profileArticles.sortedByDescending { it.likes }
                            .take(2)
                            .forEach { article ->
                                MyArticleItem(
                                    title = article.title,
                                    likes = article.likes
                                )
                            }
                    }
                }
            }
        }
    }
}

