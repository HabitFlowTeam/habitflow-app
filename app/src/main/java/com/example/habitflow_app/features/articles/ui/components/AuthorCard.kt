package com.example.habitflow_app.features.articles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Composable
fun AuthorCard(
    authorName: String,
    authorImageUrl: String?,
    date: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            if (authorImageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(authorImageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column {
                Text(text = authorName, fontWeight = FontWeight.Medium)
                Text(
                    text = date.toStandardDate(),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun String.toStandardDate(): String {
    return try {
        val odt = OffsetDateTime.parse(this)
        odt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()))
    } catch (e: Exception) {
        this
    }
}

