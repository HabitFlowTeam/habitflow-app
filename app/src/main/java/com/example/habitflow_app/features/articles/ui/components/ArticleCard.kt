package com.example.habitflow_app.features.articles.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme

/**
 * A preview function to display the [ArticleCard] component in the design preview.
 */
@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    HabitflowAppTheme {
        ArticleCard(
            authorName = "John Cooper",
            authorImageUrl = "https://xsgames.co/randomusers/assets/avatars/male/74.jpg",
            title = "Cómo crear una rutina matutina exitosa para maximizar la productividad diaria",
            description = "Empieza el día bien con estas técnicas probadas. La forma en que comienzas tu mañana puede determinar cómo te sentirás durante todo el día. Establecer una rutina matutina efectiva puede ayudarte a aumentar tu productividad, reducir el estrés y mejorar tu bienestar general. En este artículo, exploraremos varias estrategias que puedes implementar para crear una rutina matutina que funcione para ti.",
            articleId = "1",
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * A card component to display an article's author, title, and description.
 *
 * @param authorName The name of the article's author.
 * @param authorImageUrl The URL of the author's image to display.
 * @param title The title of the article.
 * @param description A short description or preview of the article's content.
 * @param articleId The unique identifier of the article, used for navigation.
 * @param onClick A callback function to handle click events on the card.
 * @param titleMaxLines Maximum number of lines to show for the title before adding ellipsis.
 * @param descriptionMaxLines Maximum number of lines to show for the description before adding ellipsis.
 * @param modifier An optional modifier to customize the layout.
 */
@Composable
fun ArticleCard(
    authorName: String,
    authorImageUrl: String?,
    title: String,
    description: String,
    articleId: String,
    onClick: (String) -> Unit,
    titleMaxLines: Int = 1,
    descriptionMaxLines: Int = 2,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(articleId) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = authorImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = authorName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = titleMaxLines,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = descriptionMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
