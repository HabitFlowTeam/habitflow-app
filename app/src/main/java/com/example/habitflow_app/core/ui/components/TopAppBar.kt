package com.example.habitflow_app.core.ui.components

import com.example.habitflow_app.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.habitflow_app.navigation.NavDestinations


/**
 * A top app bar component, displaying notifications, user profile, and streak count.
 *
 * @param streakCount The current streak count to display.
 * @param onNotificationsClick Callback triggered when the notifications button is clicked.
 * @param onSettingsClick Callback triggered when the settings button is clicked.
 * @param onProfileClick Callback triggered when the profile button is clicked.
 * @param modifier An optional modifier to customize the layout.
 */
@Composable
fun TopAppBar(
    navController: NavController = rememberNavController(),
    streakCount: Int,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier) {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        }

        Surface(
            modifier = Modifier.padding(start = 4.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEEEEEE)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak),
                    contentDescription = "Racha",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "$streakCount",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Box(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
            }

            IconButton(
                onClick = { navController.navigate(NavDestinations.PROFILE) }
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCE1F1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF5B6C94)
                    )
                }
            }
        }
    }
}