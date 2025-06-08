package com.example.habitflow_app.features.articles.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.habitflow_app.features.articles.ui.components.AuthorCard
import com.example.habitflow_app.features.articles.ui.components.CategoryChip
import com.example.habitflow_app.features.articles.ui.components.FloatingLikes
import com.example.habitflow_app.features.articles.ui.viewmodel.ArticleViewModel

@Composable
fun ArticleDetailScreen(
    navController: NavController,
    viewModel: ArticleViewModel
) {
    val article by viewModel.selectedArticle.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        article?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Flecha de regreso y título
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Autor y fecha
                AuthorCard(
                    authorName = it.authorName,
                    authorImageUrl = it.authorImageUrl,
                    date = it.createdAt,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Categoría
                CategoryChip(
                    category = it.categoryName ?: "",
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Imagen del artículo
                it.imageUrl?.let { imageUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // Contenido
                Text(text = it.content, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Artículo no encontrado", color = MaterialTheme.colorScheme.error)
        }
        // Likes flotante
        article?.let {
            FloatingLikes(likes = it.likesCount, modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}
