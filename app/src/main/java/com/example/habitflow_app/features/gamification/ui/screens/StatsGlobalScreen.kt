package com.example.habitflow_app.features.gamification.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitflow_app.core.ui.components.CategoryFilterRow
import com.example.habitflow_app.core.ui.theme.*
import com.example.habitflow_app.features.gamification.ui.components.LeaderboardItem
import com.example.habitflow_app.features.gamification.ui.components.LeaderboardPodium
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsGlobalViewModel

/**
 * Screen that displays the global leaderboard with user rankings.
 * Shows a podium for top 3 users and a list for remaining participants.
 * Includes category filtering functionality.
 *
 * @param viewModel ViewModel that handles leaderboard data and state management
 */

@Composable
fun StatsGlobalScreen(
    viewModel: StatsGlobalViewModel = hiltViewModel()
) {
    // Traer la información del viewmodel
    val leaderboardUsers by viewModel.leaderboardUsers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    val categories by viewModel.categories.collectAsState()

    // Definir la variable selected que faltaba
    var selected by remember { mutableStateOf(1) } // 1 para "Globales" por defecto

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        // Screen title
        Text(
            text = "Ranking Global",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtro de categorías
        CategoryFilterRow(
            categories = categories,
            selectedCategory = selectedCategory ?: "Todos",
            onCategorySelected = { category ->
                // Convertir "Todos" a null para obtener todos los resultados
                val categoryId = if (category == "Todos") null else category
                viewModel.selectCategory(categoryId)
            }
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error!!, color = Red500)
                }
            }

            leaderboardUsers.isNotEmpty() -> {
                // Obtener los tres primeros usuarios para el podio
                val topThree = leaderboardUsers.take(3)

                // Mostrar el podio según la cantidad de usuarios disponibles
                when (topThree.size) {
                    3 -> {
                        LeaderboardPodium(
                            first = Triple(
                                topThree[0].fullName,
                                topThree[0].streak,
                                topThree[0].avatarUrl
                            ),
                            second = Triple(
                                topThree[1].fullName,
                                topThree[1].streak,
                                topThree[1].avatarUrl
                            ),
                            third = Triple(
                                topThree[2].fullName,
                                topThree[2].streak,
                                topThree[2].avatarUrl
                            )
                        )
                    }

                    2 -> {
                        LeaderboardPodium(
                            first = Triple(
                                topThree[0].fullName,
                                topThree[0].streak,
                                topThree[0].avatarUrl
                            ),
                            second = Triple(
                                topThree[1].fullName,
                                topThree[1].streak,
                                topThree[1].avatarUrl
                            ),
                            third = null
                        )
                    }

                    1 -> {
                        LeaderboardPodium(
                            first = Triple(
                                topThree[0].fullName,
                                topThree[0].streak,
                                topThree[0].avatarUrl
                            ),
                            second = null,
                            third = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista del resto de usuarios (a partir del 4to o menos según corresponda)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(leaderboardUsers.drop(topThree.size)) { user ->
                        LeaderboardItem(
                            rank = user.rank,
                            name = user.fullName,
                            streak = user.streak,
                            imageUrl = user.avatarUrl,
                            isHighlighted = user.id == currentUserId
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay usuarios en el ranking")
                }
            }
        }
    }
}
