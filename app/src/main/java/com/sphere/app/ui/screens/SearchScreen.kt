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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onVideoClick: (String, String, String) -> Unit = { _, _, _ -> },
    focusTrigger: Int = 0,
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp

    val horizontalPaddingRaw = (screenWidthDp * 0.02f).dp
    val horizontalPadding = when {
        horizontalPaddingRaw < 2.dp -> 2.dp
        horizontalPaddingRaw > 24.dp -> 24.dp
        else -> horizontalPaddingRaw
    }

    val verticalPaddingRaw = (screenHeightDp * 0.005f).dp
    val verticalPadding = when {
        verticalPaddingRaw < 2.dp -> 2.dp
        verticalPaddingRaw > 8.dp -> 8.dp
        else -> verticalPaddingRaw
    }

    val searchBarHeightRaw = (screenHeightDp * 0.06f).dp
    val searchBarHeight = when {
        searchBarHeightRaw < 44.dp -> 44.dp
        searchBarHeightRaw > 64.dp -> 64.dp
        else -> searchBarHeightRaw
    }

    val actionWidthRaw = (screenWidthDp * 0.14f).dp
    val actionWidth = when {
        actionWidthRaw < 48.dp -> 48.dp
        actionWidthRaw > 80.dp -> 80.dp
        else -> actionWidthRaw
    }

    var aiExpanded by remember { mutableStateOf(false) }

    // Only auto-focus when trigger value changes (double tap on search tab)
    LaunchedEffect(focusTrigger) {
        if (focusTrigger > 0) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!aiExpanded) {
                        val pillShape = RoundedCornerShape(50.dp)

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = horizontalPadding, top = verticalPadding, end = horizontalPadding, bottom = verticalPadding)
                                .height(searchBarHeight)
                                .animateContentSize()
                                .shadow(6.dp, pillShape),
                            shape = pillShape,
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "Search videos",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(searchBarHeight)
                                    .clip(pillShape)
                                    .padding(horizontal = 6.dp)
                                    .focusRequester(focusRequester),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                ),
                                singleLine = true,
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "Clear",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else {
                                        IconButton(onClick = { /* voice search */ }) {
                                            Icon(
                                                Icons.Filled.Mic,
                                                contentDescription = "Voice search",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = horizontalPadding, top = verticalPadding, end = horizontalPadding, bottom = verticalPadding)
                                .height(searchBarHeight)
                                .animateContentSize(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "AI Search",
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Start
                                )

                                IconButton(onClick = { aiExpanded = false }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Close AI",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                },
                actions = {
                    // When AI is expanded show collapsed search button; otherwise show mic
                    if (aiExpanded) {
                        Surface(
                            modifier = Modifier
                                .padding(top = verticalPadding, end = horizontalPadding, bottom = verticalPadding)
                                .height(searchBarHeight)
                                .width(actionWidth)
                                .shadow(4.dp, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            tonalElevation = 4.dp,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(onClick = { aiExpanded = false }) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Restore search",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Sparkle / AI toggle button (right)
                    Surface(
                        modifier = Modifier
                            .padding(top = verticalPadding, end = horizontalPadding, bottom = verticalPadding)
                            .height(searchBarHeight)
                            .width(searchBarHeight)
                            .shadow(4.dp, CircleShape),
                        shape = CircleShape,
                        color = if (aiExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            IconButton(onClick = { aiExpanded = !aiExpanded }) {
                                Icon(
                                    Icons.Filled.AutoAwesome,
                                    contentDescription = "Toggle AI",
                                    tint = if (aiExpanded) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Results or Placeholder
            if (searchQuery.isEmpty()) {
                // Empty state with suggestions
                SearchEmptyState()
            } else {
                // Search results
                SearchResults(
                    searchQuery = searchQuery,
                    onVideoClick = onVideoClick
                )
            }
        }
    }
}

@Composable
fun SearchEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for videos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start typing to find videos, channels, and playlists",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SearchResults(
    searchQuery: String,
    onVideoClick: (String, String, String) -> Unit
) {
    val sampleVideoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Show search results
        items(15) { index ->
            SearchResultVideoItem(
                title = "Search Result: $searchQuery - Video Title $index",
                channelName = "Channel Name $index",
                views = "${(100..999).random()}K views",
                time = "${(1..11).random()} months ago",
                duration = "${(1..10).random()}:${(10..59).random()}",
                onClick = {
                    val encodedUrl = URLEncoder.encode(
                        sampleVideoUrl,
                        StandardCharsets.UTF_8.toString()
                    )
                    onVideoClick(
                        encodedUrl,
                        "Search Result: $searchQuery - Video $index",
                        "Channel Name $index"
                    )
                }
            )
        }
    }
}

@Composable
fun SearchResultVideoItem(
    title: String,
    channelName: String,
    views: String,
    time: String,
    duration: String,
    onClick: () -> Unit
) {
    val seed = title.hashCode()
    val thumbnailUrl = "https://picsum.photos/seed/$seed/640/360"
    val avatarUrl = "https://picsum.photos/seed/${seed + 1}/100/100"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        // Thumbnail Section
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery)
            )

            // Duration Badge
            Surface(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = duration,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        // Details Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Channel Avatar
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$channelName • $views • $time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // More Options Icon
            IconButton(
                onClick = { /* Show options menu */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
