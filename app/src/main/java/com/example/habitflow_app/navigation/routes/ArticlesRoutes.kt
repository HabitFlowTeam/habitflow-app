package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.articles.ui.screens.ArticleDetailScreen
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel

@Composable
fun ArticleDetailRoute(
    navController: NavController,
    articleId: String,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    // Selecciona el artículo antes de mostrar la pantalla
    viewModel.selectArticleById(articleId)
    ArticleDetailScreen(
        navController = navController,
        viewModel = viewModel
    )
}
