package com.example.habitflow_app.features.gamification.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.habitflow_app.R
import com.example.habitflow_app.core.ui.theme.*

/**
 * A leaderboard row displaying a user's rank, avatar or initial, name, and points with a streak icon.
 *
 * @param rank The user's current rank in the leaderboard.
 * @param name The name of the user to display.
 * @param streak The total points or streak value to display.
 * @param imageUrl Optional URL for the user's avatar image. If null, the first letter of the name is used.
 * @param modifier Modifier to customize layout and appearance.
 * @param isHighlighted Whether the item should be visually highlighted (e.g., for current user).
 */
@Composable
fun LeaderboardItem(
    rank: Int,
    name: String,
    streak: Int,
    imageUrl: String? = null,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val backgroundColor = if (isHighlighted) Yellow100 else White

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left section: Rank + Avatar + Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Rank number
            Text(
                text = "$rank",
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Zinc400
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
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
                        style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = White
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name
            Text(
                text = name,
                style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Black
            )
        }

        // Right section: Points with streak icon
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
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderboardItemPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        LeaderboardItem(
            rank = 1,
            name = "Laura M.",
            streak = 210,
            imageUrl = null,
            isHighlighted = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        LeaderboardItem(rank = 2, name = "Carlos R.", streak = 180, imageUrl = null)
    }
}
