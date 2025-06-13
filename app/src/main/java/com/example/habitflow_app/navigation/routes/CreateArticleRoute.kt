package com.example.habitflow_app.navigation.routes

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.habitflow_app.features.articles.ui.screens.CreateArticleScreen
import com.example.habitflow_app.features.articles.ui.viewmodel.CreateArticleViewModel

@Composable
fun CreateArticleRoute(
    navController: NavController,
    viewModel: CreateArticleViewModel = hiltViewModel()
) {
    CreateArticleScreen(
        navController = navController,
        viewModel = viewModel
    )
}