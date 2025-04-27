package com.example.habitflow_app.features.authentication.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.habitflow_app.BuildConfig.LOGO_URL

@Composable
fun Logo() {
    AsyncImage(
        model = LOGO_URL,
        contentDescription = "logo",
        modifier = Modifier.height(100.dp).width(240.dp)
    )
}