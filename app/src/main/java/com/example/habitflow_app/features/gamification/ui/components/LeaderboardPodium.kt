package com.example.habitflow_app.features.gamification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.habitflow_app.core.ui.theme.*
import com.example.habitflow_app.R

/**
 * Displays a podium layout with the top 3 users.
 *
 * @param first Triple containing the name, streak, and optional image URL of the 1st place user.
 * @param second Triple containing the name, streak, and optional image URL of the 2nd place user.
 * @param third Triple containing the name, streak, and optional image URL of the 3rd place user.
 * @param modifier Modifier for styling the overall layout.
 */
@Composable
fun LeaderboardPodium(
    first: Triple<String, Int, String?>,
    second: Triple<String, Int, String?>?,
    third: Triple<String, Int, String?>?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // Segundo lugar (izquierda)
        if (second != null) {
            PodiumItem(
                position = 2,
                name = second.first,
                streak = second.second,
                imageUrl = second.third
            )
        } else {
            // Espacio vacío para mantener la estructura
            Spacer(modifier = Modifier.width(72.dp))
        }

        // Primer lugar (centro)
        PodiumItem(
            position = 1,
            name = first.first,
            streak = first.second,
            imageUrl = first.third
        )

        // Tercer lugar (derecha)
        if (third != null) {
            PodiumItem(
                position = 3,
                name = third.first,
                streak = third.second,
                imageUrl = third.third
            )
        } else {
            // Espacio vacío para mantener la estructura
            Spacer(modifier = Modifier.width(72.dp))
        }
    }
}

/**
 * Displays a single podium item with a user avatar, crown/icon, name, streak, and position badge.
 *
 * @param position The user's rank (1st, 2nd, or 3rd).
 * @param name The name of the user.
 * @param streak The streak value of the user.
 * @param imageUrl Optional URL to load the user's avatar. Falls back to first letter of name.
 * @param modifier Modifier to customize this item layout.
 */
@Composable
fun PodiumItem(
    position: Int,
    name: String,
    streak: Int,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val crownColor = when (position) {
        1 -> Yellow500
        2 -> Zinc300
        3 -> Yellow200
        else -> Zinc400
    }

    val crownSize = when (position) {
        1 -> 32.dp
        2 -> 24.dp
        3 -> 20.dp
        else -> 16.dp
    }

    val avatarSize = when (position) {
        1 -> 84.dp
        else -> 72.dp
    }

    val badgeColor = when (position) {
        1 -> Yellow500
        2 -> Zinc200
        3 -> Yellow200
        else -> Zinc300
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
            contentDescription = "Position $position crown",
            tint = crownColor,
            modifier = Modifier.size(crownSize)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(crownColor.copy(alpha = 0.15f))
                .border(3.dp, crownColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Avatar de $name",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(
                    text = name.take(1).uppercase(),
                    style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Black
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_streak),
                contentDescription = "Racha",
                tint = Yellow500,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$streak",
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$position",
                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPodiumPreview() {
    LeaderboardPodium(
        first = Triple("Juan P.", 256, null),
        second = Triple("María L.", 198, null),
        third = Triple("Carlos R.", 145, null)
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPodiumWithTwoUsersPreview() {
    LeaderboardPodium(
        first = Triple("Juan P.", 256, null),
        second = Triple("María L.", 198, null),
        third = null
    )
}

@Preview(showBackground = true)
@Composable
fun LeaderboardPodiumWithOneUserPreview() {
    LeaderboardPodium(
        first = Triple("Juan P.", 256, null),
        second = null,
        third = null
    )
}