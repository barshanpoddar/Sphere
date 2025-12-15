package com.sphere.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleFilled
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(onVideoClick: (String, String, String) -> Unit) {
    val sampleVideoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) { index ->
            VideoCard(
                    title = "Sample Video Title $index",
                    channelName = "Channel $index",
                    onClick = {
                        val encodedUrl =
                                URLEncoder.encode(sampleVideoUrl, StandardCharsets.UTF_8.toString())
                        onVideoClick(encodedUrl, "Sample Video Title $index", "Channel $index")
                    }
            )
        }
    }
}

@Composable
fun VideoCard(title: String, channelName: String, onClick: () -> Unit) {
    // Generate a deterministic random-like image based on title/length to have variety
    val seed = title.hashCode()
    val thumbnailUrl = "https://picsum.photos/seed/$seed/640/360"

    Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Video Thumbnail",
                        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
                        contentScale = ContentScale.Crop
                )
                Icon(
                        imageVector = Icons.Default.PlayCircleFilled,
                        contentDescription = "Play",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.height(48.dp).aspectRatio(1f)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = channelName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
