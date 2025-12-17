package com.sphere.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Action Pills
            ActionPills()
        }
        item {
            // History Section
            HistorySection()
        }
        item {
            // Playlists Section
            PlaylistsSection()
        }
        item {
            // Content Management Section
            ContentManagementSection()
        }
        item {
            // Settings & Utilities
            SettingsUtilitiesSection()
        }
    }
}

@Composable
fun ActionPills() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionPill("Switch Account", Icons.Default.AccountCircle)
        ActionPill("Google Account", Icons.Default.AccountCircle)
        ActionPill("Incognito", Icons.Default.PrivacyTip)
        ActionPill("Share Profile", Icons.Default.Share)
    }
}

@Composable
fun ActionPill(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.clickable { /* TODO: handle click */ },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(16.dp))
            Text(text = text, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun HistorySection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Recently Viewed", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { /* TODO: View All */ }) {
                Text("View All")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(5) { index ->
                HistoryVideoCard("History Video $index")
            }
        }
    }
}

@Composable
fun HistoryVideoCard(title: String) {
    val thumbnailUrl = "https://picsum.photos/seed/${title.hashCode()}/160/90"
    Column(modifier = Modifier.width(160.dp)) {
        Card(
            modifier = Modifier.aspectRatio(16 / 9f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = "Video Thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall, maxLines = 2)
    }
}

@Composable
fun PlaylistsSection() {
    Column {
        Text(text = "Playlists", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PlaylistItem("Liked Videos", Icons.Default.ThumbUp)
        PlaylistItem("Watch Later", Icons.Default.WatchLater)
        PlaylistItem("My Playlist", Icons.AutoMirrored.Filled.PlaylistPlay)
    }
}

@Composable
fun PlaylistItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: handle click */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(24.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ContentManagementSection() {
    Column {
        Text(text = "Content Management", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ContentItem("Your Videos", Icons.Default.VideoLibrary)
        ContentItem("Downloads", Icons.Default.Download)
        ContentItem("Your Movies", Icons.Default.Movie)
    }
}

@Composable
fun ContentItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: handle click */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(24.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsUtilitiesSection() {
    Column {
        Text(text = "Settings & Utilities", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        UtilityItem("Settings", Icons.Default.Settings)
        UtilityItem("Time Watched", Icons.Default.History)
        UtilityItem("Help & Feedback", Icons.AutoMirrored.Filled.Help)
    }
}

@Composable
fun UtilityItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: handle click */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(24.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
