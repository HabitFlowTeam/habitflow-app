package com.example.habitflow_app.features.articles.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.features.articles.ui.components.ArticleCard
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.navigation.NavDestinations

@Composable
fun ArticlesMainScreen(
    navController: NavController,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val userArticles by viewModel.userArticles.collectAsState()
    val otherArticles by viewModel.otherArticles.collectAsState()
    val isLoading by viewModel.allArticlesIsLoading.collectAsState()
    val error by viewModel.allArticlesError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllArticles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Artículos",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )

            Button(
                onClick = { navController.navigate(NavDestinations.CREATE_ARTICLE) },
                contentPadding = PaddingValues(horizontal = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir artículo",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nuevo artículo")
            }
        }

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
                    Text("Error: $error")
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (userArticles.isNotEmpty()) {
                        item {
                            Text(
                                text = "Mis artículos",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(userArticles) { article ->
                            ArticleCard(
                                authorName = article.authorName,
                                authorImageUrl = article.authorImageUrl,
                                title = article.title,
                                description = article.content,
                                articleId = article.articleId,
                                onClick = { id ->
                                    navController.navigate(NavDestinations.articleDetailRoute(id))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else {
                        item {
                            Text(
                                text = "Mis artículos",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Text(
                                    text = "Aún no has creado ningún artículo.\n¡Comparte tu primera idea!",
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    item {
                        Text(
                            text = "Otros artículos",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    if (otherArticles.isNotEmpty()) {
                        items(otherArticles) { article ->
                            ArticleCard(
                                authorName = article.authorName,
                                authorImageUrl = article.authorImageUrl,
                                title = article.title,
                                description = article.content,
                                articleId = article.articleId,
                                onClick = { id ->
                                    navController.navigate(NavDestinations.articleDetailRoute(id))
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF5F5F5)
                                )
                            ) {
                                Text(
                                    text = "No hay otros artículos disponibles.",
                                    modifier = Modifier.padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}