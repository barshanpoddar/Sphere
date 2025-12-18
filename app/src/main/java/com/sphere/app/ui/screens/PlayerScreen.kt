package com.sphere.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sphere.app.ui.components.VideoPlayer

@Composable
fun PlayerScreen(videoUrl: String, title: String, channelName: String) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // 1. Video Player attached to top
        VideoPlayer(videoUrl = videoUrl)

        // 2. Scrollable Content
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { VideoInfoSection(title = title) }
            item { ActionButtonsRow() }
            item { ChannelInfoRow(channelName = channelName) }
            item { CommentsPreviewSection() }

            // 3. Recommended Videos
            items(10) { index -> RecommendedVideoItem(index) }
        }
    }
}

@Composable
fun VideoInfoSection(title: String) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text(
                text = title,
                style =
                        MaterialTheme.typography.titleLarge.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                        ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                    text = "1.2M views",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                    text = "• 2 years ago",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                    text = "...more",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ActionButtonsRow() {
    val scrollState = rememberScrollState()
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton(icon = Icons.Default.ThumbUp, label = "45K")
        ActionButton(icon = Icons.Default.ThumbDown, label = "")
        ActionButton(icon = Icons.Default.Share, label = "Share")
        ActionButton(icon = Icons.Default.Download, label = "Download")
        ActionButton(icon = Icons.Default.Check, label = "Save")
    }
}

@Composable
fun ActionButton(icon: ImageVector, label: String) {
    Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.height(36.dp),
            onClick = {}
    ) {
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface
            )
            if (label.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ChannelInfoRow(channelName: String) {
        Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            // Channel Avatar (Dummy)
            AsyncImage(
                    model = "https://picsum.photos/seed/${channelName.hashCode()}/100/100",
                    contentDescription = "Channel Avatar",
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray),
                    contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = channelName,
                        style =
                                MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium
                                )
                )
                Text(
                        text = "105K subscribers",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                    onClick = {},
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.surface
                            ),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding =
                            androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) { Text(text = "Subscribe", style = MaterialTheme.typography.labelLarge) }
        }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
    }
}

@Composable
fun CommentsPreviewSection() {
    Column(modifier = Modifier.fillMaxWidth().clickable {}.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                    text = "Comments",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                    text = "245",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                    text = "This is a truly amazing video! Learned so much.",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RecommendedVideoItem(index: Int) {
    Column(modifier = Modifier.clickable {}) {
        // Thumbnail
        AsyncImage(
                model = "https://picsum.photos/seed/${index + 100}/640/360",
                contentDescription = "Video Thumbnail",
                modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
                contentScale = ContentScale.Crop
        )

        // Meta
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                    model = "https://picsum.photos/seed/${index + 200}/100/100",
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.Gray),
                    contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                        text = "Recommended Video Title $index - Must Watch!",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                        text = "Channel Name • 54K views • 5 hours ago",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
