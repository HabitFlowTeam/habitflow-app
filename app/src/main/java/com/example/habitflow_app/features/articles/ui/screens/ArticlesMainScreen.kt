package com.example.habitflow_app.features.articles.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitflow_app.features.articles.ui.components.ArticleCard
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArticlesMainScreen(
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val articles by viewModel.allArticles.collectAsState()
    val isLoading by viewModel.allArticlesIsLoading.collectAsState()
    val error by viewModel.allArticlesError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAllArticles()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Recomendados para ti",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        when {
            isLoading -> Text("Cargando artículos...")
            error != null -> Text("Error: $error")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(articles) { article ->
                    ArticleCard(
                        authorName = article.authorName,
                        authorImageUrl = article.authorImageUrl,
                        title = article.title,
                        description = article.content,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}