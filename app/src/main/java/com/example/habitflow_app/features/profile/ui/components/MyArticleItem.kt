package com.example.habitflow_app.features.profile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.Red500
import com.example.habitflow_app.core.ui.theme.Zinc500

/**
 * A composable function that represents an article item with a title, likes, and an optional image.
 *
 * @param title The title of the article.
 * @param likes The number of likes the article has received.
 * @param imageUrl The URL of the article's image (optional).
 * @param modifier An optional [Modifier] to customize the layout of the component.
 * @param backgroundColor The background color of the card.
 * @param contentColor The color of the content inside the card.
 * @param onItemClick A callback triggered when the card is clicked.
 */
@Composable
fun MyArticleItem(
        title: String,
        likes: Int,
        imageUrl: String? = null,
        modifier: Modifier = Modifier,
        backgroundColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        onItemClick: () -> Unit = {}
) {
    Card(
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors =
                    CardDefaults.cardColors(
                            containerColor = backgroundColor,
                            contentColor = contentColor
                    ),
            onClick = onItemClick
    ) {
        Column {
            imageUrl?.let { url ->
                AsyncImage(
                        model = url,
                        contentDescription = "Imagen del artículo: $title",
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = title,
                        style = AppTypography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Likes",
                                modifier = Modifier.size(20.dp),
                                tint = Red500
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                                text = likes.toString(),
                                style = AppTypography.bodyMedium,
                                color = Zinc500
                        )
                    }
                }
            }
        }
    }
}
