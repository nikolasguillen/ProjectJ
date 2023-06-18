package com.example.projectj.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.projectj.data.remote.dto.MangaDetails
import com.example.projectj.data.remote.dto.RankedMangaData

@Composable
fun ProjectJMangaListItem(
    manga: MangaDetails,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(CardDefaults.shape)
            .clickable {
                navigateToDetail(manga.id)
            }
            .clip(CardDefaults.shape)
            .height(170.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOpened) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.3f)
                    .width(100.dp)
            ) {
                AsyncImage(
                    model = manga.mainPicture.medium,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(
                            MaterialTheme.shapes.medium.copy(
                                topStart = CornerSize(16.dp),
                                bottomStart = CornerSize(16.dp),
                                topEnd = CornerSize(0.dp),
                                bottomEnd = CornerSize(0.dp)
                            )
                        )
                        .fillMaxSize()
                )
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${manga.rank}. ${manga.title}",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )
                Text(
                    text = "Autori: ${
                        manga.authors.joinToString(
                            limit = 2,
                            truncated = " and others..."
                        ) { "${it.authorDetails.firstName ?: ""} ${it.authorDetails.lastName ?: ""}" }
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )
                Text(
                    text = "Genere: ${manga.genres.joinToString(limit = 3, truncated = "") { it.name }}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )
            }
        }
    }
}