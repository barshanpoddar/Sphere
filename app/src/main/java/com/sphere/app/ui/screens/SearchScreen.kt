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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.Dp
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

    // State for AI search query
    var aiSearchQuery by remember { mutableStateOf("") }
    val aiFocusRequester = remember { FocusRequester() }

    // Animation specs
    val animationSpec = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val pillShape = RoundedCornerShape(50.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = horizontalPadding, top = verticalPadding, end = 0.dp, bottom = verticalPadding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Search Bar - Animated width
                        AnimatedVisibility(
                            visible = !aiExpanded,
                            enter = expandHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                expandFrom = Alignment.Start
                            ) + fadeIn(animationSpec = tween(200)),
                            exit = shrinkHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                shrinkTowards = Alignment.Start
                            ) + fadeOut(animationSpec = tween(150)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(searchBarHeight)
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
                                            "Search",
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
                        }

                        // Collapsed Search Button (circular) - shows when AI is expanded
                        AnimatedVisibility(
                            visible = aiExpanded,
                            enter = expandHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                expandFrom = Alignment.Start
                            ) + fadeIn(animationSpec = tween(200)),
                            exit = shrinkHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                shrinkTowards = Alignment.Start
                            ) + fadeOut(animationSpec = tween(150))
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(searchBarHeight)
                                    .shadow(4.dp, CircleShape),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 4.dp,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                                onClick = { aiExpanded = false }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Switch to search",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // AI Search Bar - Animated (shows when AI is expanded)
                        AnimatedVisibility(
                            visible = aiExpanded,
                            enter = expandHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                expandFrom = Alignment.End
                            ) + fadeIn(animationSpec = tween(200)),
                            exit = shrinkHorizontally(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                shrinkTowards = Alignment.End
                            ) + fadeOut(animationSpec = tween(150)),
                            modifier = Modifier.weight(1f)
                        ) {
                            val aiPillShape = RoundedCornerShape(50.dp)
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(searchBarHeight)
                                    .shadow(6.dp, aiPillShape),
                                shape = aiPillShape,
                                color = MaterialTheme.colorScheme.surface,
                                tonalElevation = 6.dp,
                                border = BorderStroke(
                                    2.dp, 
                                    androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFE040FB),
                                            Color(0xFF7C4DFF),
                                            Color(0xFF448AFF),
                                            Color(0xFF18FFFF)
                                        )
                                    )
                                )
                            ) {
                                TextField(
                                    value = aiSearchQuery,
                                    onValueChange = { aiSearchQuery = it },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "AI Search",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    placeholder = {
                                        Text(
                                            "Talk to YouTube",
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(searchBarHeight)
                                        .clip(aiPillShape)
                                        .padding(horizontal = 6.dp)
                                        .focusRequester(aiFocusRequester),
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
                                        if (aiSearchQuery.isNotEmpty()) {
                                            IconButton(onClick = { aiSearchQuery = "" }) {
                                                Icon(
                                                    Icons.Default.Clear,
                                                    contentDescription = "Clear",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                },
                actions = {
                    // AI toggle button (circular) - shows when search bar is expanded
                    AnimatedVisibility(
                        visible = !aiExpanded,
                        enter = expandHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            expandFrom = Alignment.End
                        ) + fadeIn(animationSpec = tween(200)),
                        exit = shrinkHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            shrinkTowards = Alignment.End
                        ) + fadeOut(animationSpec = tween(150))
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(top = verticalPadding, end = horizontalPadding, bottom = verticalPadding)
                                .size(searchBarHeight)
                                .shadow(4.dp, CircleShape),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            onClick = { aiExpanded = true }
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.AutoAwesome,
                                    contentDescription = "Switch to AI Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
