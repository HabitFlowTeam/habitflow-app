package com.example.habitflow_app.features.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Blue400
import com.example.habitflow_app.core.ui.theme.Blue500
import com.example.habitflow_app.core.ui.theme.White
import com.example.habitflow_app.core.ui.theme.Zinc500

/**
 * A composable function that displays a profile header with an avatar, name, and username.
 *
 * @param name The full name of the user.
 * @param username The username of the user.
 * @param imageUrl The URL of the user's profile image (optional).
 * @param modifier An optional [Modifier] to customize the layout of the component.
 */
@Composable
fun ProfileHeader(
    name: String,
    username: String,
    imageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar - Circular container
        Box(
            modifier = modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Blue500)
                .border(2.dp, Blue400, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Avatar de $name",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = name.take(1).uppercase(),
                    style = AppTypography.displayMedium,
                    color = White
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = name,
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Username
        Text(
            text = username,
            style = AppTypography.bodyLarge,
            color = Zinc500
        )
    }
}