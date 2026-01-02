package com.sphere.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.statusBarsPadding
// window inset helpers
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onVideoClick: (String, String, String) -> Unit,
  onProfileClick: () -> Unit = {},
) {
  val sampleVideoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

  Scaffold(
    topBar = {
      Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column {
          TopAppBar(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().height(56.dp),
            title = {
              Text(
                text = "Sphere",
                modifier = Modifier.padding(start = 16.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              )
            },
            actions = {
              IconButton(onClick = onProfileClick) {
                Icon(
                  imageVector = Icons.Default.AccountCircle,
                  contentDescription = "You",
                  modifier = Modifier.size(32.dp),
                )
              }
            },
            colors =
            TopAppBarDefaults.topAppBarColors(
              containerColor = Color.Transparent,
              titleContentColor = MaterialTheme.colorScheme.onSurface,
              actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            ),
          )
        }
      }
    },
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().padding(paddingValues),
      contentPadding = PaddingValues(bottom = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      items(10) { index ->
        VideoItem(
          title =
            "Amazing Video Title That Might wrap to two lines because it is long $index",
          channelName = "Channel $index",
          views = "${(1..999).random()}K views",
          time = "${(1..11).random()} months ago",
          duration = "${(1..10).random()}:00",
          onClick = {
            val encodedUrl =
              URLEncoder.encode(
                sampleVideoUrl,
                StandardCharsets.UTF_8.toString(),
              )
            onVideoClick(
              encodedUrl,
              "Amazing Video Title... $index",
              "Channel $index",
            )
          },
        )
      }
    }
  }
}

@Composable
fun VideoItem(
  title: String,
  channelName: String,
  views: String,
  time: String,
  duration: String,
  onClick: () -> Unit,
) {
  // Deterministic random seed for consistent thumbnails per item
  val seed = title.hashCode()
  val thumbnailUrl = "https://picsum.photos/seed/$seed/640/360"
  val avatarUrl = "https://picsum.photos/seed/${seed + 1}/100/100"

  Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
    // Thumbnail Section
    Box(contentAlignment = Alignment.BottomEnd) {
      AsyncImage(
        model = thumbnailUrl,
        contentDescription = "Thumbnail",
        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
        contentScale = ContentScale.Crop,
        // Add a placeholder background to avoid white flashes
        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
      )

      // Duration Badge
      Surface(
        color = Color.Black.copy(alpha = 0.8f),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(8.dp),
      ) {
        Text(
          text = duration,
          color = Color.White,
          style = MaterialTheme.typography.labelSmall,
          modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
        )
      }
    }

    // Details Section including Avatar
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.Top) {
      // Channel Avatar
      AsyncImage(
        model = avatarUrl,
        contentDescription = "Avatar",
        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.Gray),
        contentScale = ContentScale.Crop,
      )

      Spacer(modifier = Modifier.width(12.dp))

      // Text Info
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodyLarge,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "$channelName • $views • $time",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 2,
        )
      }

      // More Options Icon
      IconButton(onClick = { /* Show options menu */ }, modifier = Modifier.size(24.dp)) {
        Icon(
          Icons.Default.MoreVert,
          contentDescription = "More",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}
