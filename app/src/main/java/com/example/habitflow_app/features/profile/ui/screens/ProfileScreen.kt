package com.example.habitflow_app.features.profile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.habitflow_app.core.ui.components.TopAppBar
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Background
import com.example.habitflow_app.features.profile.ui.components.MyArticleItem
import com.example.habitflow_app.features.profile.ui.components.ProfileHeader
import com.example.habitflow_app.features.profile.ui.components.ProfileStats

/**
 * A preview of the ProfileScreen composable for testing in the editor.
 */
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen(){
    ProfileScreen()
}

/**
 * A composable function that displays the profile screen, including the top app bar,
 * profile header, statistics, and a list of articles.
 */
@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {

        //TopBar
        TopAppBar(
            streakCount = 2,
            onNotificationsClick = {},
            onSettingsClick = {},
            onProfileClick = {}
        )

        // Row with back arrow and screen title
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "arrowBack")

            Text(text = "Mi perfil",
                style =AppTypography.titleLarge,)

        }

        // Profile header with user information
        ProfileHeader(
            name = "Sarah Johnson",
            username = "@sarahj"
        )

        // Profile statistics
        ProfileStats(
            currentStreak = 10,
            bestStreak = 32,
            completedHabits = 100
        )

        // List of articles
        MyArticleItem(
            title = "Meditación matutina en 1 minuto",
            likes = 24
        )

        MyArticleItem(
            title = "Cómo mantener una rutina de ejercicio",
            likes = 18
        )
    }
}