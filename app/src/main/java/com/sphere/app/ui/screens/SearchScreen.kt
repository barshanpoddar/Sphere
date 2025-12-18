package com.sphere.app.ui.screens

import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
        val horizontalPadding =
                when {
                        horizontalPaddingRaw < 2.dp -> 2.dp
                        horizontalPaddingRaw > 24.dp -> 24.dp
                        else -> horizontalPaddingRaw
                }

        val verticalPaddingRaw = (screenHeightDp * 0.005f).dp
        val verticalPadding =
                when {
                        verticalPaddingRaw < 2.dp -> 2.dp
                        verticalPaddingRaw > 8.dp -> 8.dp
                        else -> verticalPaddingRaw
                }

        val searchBarHeightRaw = (screenHeightDp * 0.06f).dp
        val searchBarHeight =
                when {
                        searchBarHeightRaw < 44.dp -> 44.dp
                        searchBarHeightRaw > 64.dp -> 64.dp
                        else -> searchBarHeightRaw
                }

        val searchBarGap = 12.dp
        val widthBuffer = 32.dp
        val expandedBarWidth =
                (screenWidthDp -
                                (horizontalPadding.value * 2) -
                                searchBarHeight.value -
                                searchBarGap.value -
                                widthBuffer.value)
                        .dp

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

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        val pillShape = RoundedCornerShape(50.dp)

                                        // Animate search bar width
                                        val searchBarWidth by
                                                animateDpAsState(
                                                        targetValue =
                                                                if (!aiExpanded) expandedBarWidth
                                                                else searchBarHeight,
                                                        animationSpec =
                                                                spring(
                                                                        dampingRatio =
                                                                                Spring.DampingRatioLowBouncy,
                                                                        stiffness = 50f
                                                                ),
                                                        label = "searchBarWidth"
                                                )

                                        // Animate AI search bar width
                                        var isAiFullyExpanded by remember { mutableStateOf(false) }
                                        val aiBarWidth by
                                                animateDpAsState(
                                                        targetValue =
                                                                if (aiExpanded) expandedBarWidth
                                                                else searchBarHeight,
                                                        animationSpec =
                                                                spring(
                                                                        dampingRatio =
                                                                                Spring.DampingRatioLowBouncy,
                                                                        stiffness = 50f
                                                                ),
                                                        label = "aiBarWidth",
                                                        finishedListener = { finalWidth ->
                                                                isAiFullyExpanded =
                                                                        aiExpanded &&
                                                                                finalWidth >=
                                                                                        expandedBarWidth
                                                        }
                                                )

                                        // Rotation animation for the gradient border
                                        val infiniteTransition =
                                                rememberInfiniteTransition(label = "borderRotation")
                                        val rotation by
                                                infiniteTransition.animateFloat(
                                                        initialValue = 0f,
                                                        targetValue = 360f,
                                                        animationSpec =
                                                                infiniteRepeatable(
                                                                        animation = tween(2000),
                                                                        repeatMode =
                                                                                RepeatMode.Restart
                                                                ),
                                                        label = "rotation"
                                                )

                                        Row(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .padding(
                                                                        start = horizontalPadding,
                                                                        top = verticalPadding,
                                                                        end = horizontalPadding,
                                                                        bottom = verticalPadding
                                                                ),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement =
                                                        Arrangement.spacedBy(searchBarGap)
                                        ) {
                                                // Regular Search Bar / Button
                                                Surface(
                                                        modifier =
                                                                Modifier.width(searchBarWidth)
                                                                        .widthIn(
                                                                                min =
                                                                                        searchBarHeight
                                                                        )
                                                                        .height(searchBarHeight)
                                                                        .shadow(
                                                                                if (!aiExpanded)
                                                                                        6.dp
                                                                                else 4.dp,
                                                                                if (!aiExpanded)
                                                                                        pillShape
                                                                                else CircleShape
                                                                        ),
                                                        shape =
                                                                if (!aiExpanded) pillShape
                                                                else CircleShape,
                                                        color = MaterialTheme.colorScheme.surface,
                                                        tonalElevation =
                                                                if (!aiExpanded) 6.dp else 4.dp,
                                                        border =
                                                                BorderStroke(
                                                                        1.dp,
                                                                        MaterialTheme.colorScheme
                                                                                .surfaceVariant
                                                                ),
                                                        onClick = {
                                                                if (aiExpanded) aiExpanded = false
                                                        }
                                                ) {
                                                        Box(modifier = Modifier.fillMaxSize()) {
                                                                AnimatedContent(
                                                                        targetState = aiExpanded,
                                                                        transitionSpec = {
                                                                                fadeIn(
                                                                                        animationSpec =
                                                                                                tween(
                                                                                                        durationMillis =
                                                                                                                500,
                                                                                                        delayMillis =
                                                                                                                150
                                                                                                )
                                                                                ) togetherWith
                                                                                        fadeOut(
                                                                                                animationSpec =
                                                                                                        tween(
                                                                                                                durationMillis =
                                                                                                                        250
                                                                                                        )
                                                                                        )
                                                                        },
                                                                        label = "searchBarContent"
                                                                ) { isAiExpanded ->
                                                                        if (!isAiExpanded) {
                                                                                // Full
                                                                                // Search
                                                                                // Bar
                                                                                TextField(
                                                                                        value =
                                                                                                searchQuery,
                                                                                        onValueChange = {
                                                                                                searchQuery =
                                                                                                        it
                                                                                        },
                                                                                        leadingIcon = {
                                                                                                Icon(
                                                                                                        Icons.Default
                                                                                                                .Search,
                                                                                                        contentDescription =
                                                                                                                "Search",
                                                                                                        tint =
                                                                                                                MaterialTheme
                                                                                                                        .colorScheme
                                                                                                                        .onSurfaceVariant,
                                                                                                        modifier =
                                                                                                                Modifier.size(
                                                                                                                        20.dp
                                                                                                                )
                                                                                                )
                                                                                        },
                                                                                        placeholder = {
                                                                                                Text(
                                                                                                        "Search",
                                                                                                        color =
                                                                                                                MaterialTheme
                                                                                                                        .colorScheme
                                                                                                                        .onSurfaceVariant
                                                                                                )
                                                                                        },
                                                                                        modifier =
                                                                                                Modifier.fillMaxSize()
                                                                                                        .clip(
                                                                                                                pillShape
                                                                                                        )
                                                                                                        .padding(
                                                                                                                horizontal =
                                                                                                                        6.dp
                                                                                                        )
                                                                                                        .focusRequester(
                                                                                                                focusRequester
                                                                                                        ),
                                                                                        colors =
                                                                                                TextFieldDefaults
                                                                                                        .colors(
                                                                                                                focusedContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                unfocusedContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                disabledContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                focusedIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                                unfocusedIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                                disabledIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                        ),
                                                                                        singleLine =
                                                                                                true,
                                                                                        trailingIcon = {
                                                                                                if (searchQuery
                                                                                                                .isNotEmpty()
                                                                                                ) {
                                                                                                        IconButton(
                                                                                                                onClick = {
                                                                                                                        searchQuery =
                                                                                                                                ""
                                                                                                                }
                                                                                                        ) {
                                                                                                                Icon(
                                                                                                                        Icons.Default
                                                                                                                                .Clear,
                                                                                                                        contentDescription =
                                                                                                                                "Clear",
                                                                                                                        tint =
                                                                                                                                MaterialTheme
                                                                                                                                        .colorScheme
                                                                                                                                        .onSurfaceVariant,
                                                                                                                        modifier =
                                                                                                                                Modifier.size(
                                                                                                                                        18.dp
                                                                                                                                )
                                                                                                                )
                                                                                                        }
                                                                                                } else {
                                                                                                        IconButton(
                                                                                                                onClick = { /* voice search */
                                                                                                                }
                                                                                                        ) {
                                                                                                                Icon(
                                                                                                                        Icons.Filled
                                                                                                                                .Mic,
                                                                                                                        contentDescription =
                                                                                                                                "Voice search",
                                                                                                                        tint =
                                                                                                                                MaterialTheme
                                                                                                                                        .colorScheme
                                                                                                                                        .onSurfaceVariant,
                                                                                                                        modifier =
                                                                                                                                Modifier.size(
                                                                                                                                        18.dp
                                                                                                                                )
                                                                                                                )
                                                                                                        }
                                                                                                }
                                                                                        }
                                                                                )
                                                                        } else {
                                                                                // Collapsed
                                                                                // Search
                                                                                // Button
                                                                                // Icon
                                                                                Box(
                                                                                        modifier =
                                                                                                Modifier.fillMaxSize(),
                                                                                        contentAlignment =
                                                                                                Alignment
                                                                                                        .Center
                                                                                ) {
                                                                                        Icon(
                                                                                                Icons.Default
                                                                                                        .Search,
                                                                                                contentDescription =
                                                                                                        "Switch to search",
                                                                                                tint =
                                                                                                        MaterialTheme
                                                                                                                .colorScheme
                                                                                                                .onSurfaceVariant,
                                                                                                modifier =
                                                                                                        Modifier.size(
                                                                                                                20.dp
                                                                                                        )
                                                                                        )
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }

                                                // AI Search Bar / Button
                                                val aiPillShape = RoundedCornerShape(50.dp)
                                                Surface(
                                                        modifier =
                                                                Modifier.width(aiBarWidth)
                                                                        .widthIn(
                                                                                min =
                                                                                        searchBarHeight
                                                                        )
                                                                        .height(searchBarHeight)
                                                                        .shadow(
                                                                                if (aiExpanded) 6.dp
                                                                                else 4.dp,
                                                                                if (aiExpanded)
                                                                                        aiPillShape
                                                                                else CircleShape
                                                                        )
                                                                        .drawBehind {
                                                                                if (isAiFullyExpanded
                                                                                ) {
                                                                                        val glowColor =
                                                                                                Color(
                                                                                                                0xFF7C4DFF
                                                                                                        )
                                                                                                        .copy(
                                                                                                                alpha =
                                                                                                                        0.4f
                                                                                                        )
                                                                                        drawIntoCanvas {
                                                                                                canvas
                                                                                                ->
                                                                                                val paint =
                                                                                                        Paint()
                                                                                                val frameworkPaint =
                                                                                                        paint.asFrameworkPaint()
                                                                                                frameworkPaint
                                                                                                        .maskFilter =
                                                                                                        BlurMaskFilter(
                                                                                                                25f,
                                                                                                                BlurMaskFilter
                                                                                                                        .Blur
                                                                                                                        .NORMAL
                                                                                                        )
                                                                                                frameworkPaint
                                                                                                        .color =
                                                                                                        glowColor
                                                                                                                .toArgb()

                                                                                                val spread =
                                                                                                        2.dp.toPx()
                                                                                                canvas.nativeCanvas
                                                                                                        .drawRoundRect(
                                                                                                                -spread,
                                                                                                                -spread,
                                                                                                                size.width +
                                                                                                                        spread,
                                                                                                                size.height +
                                                                                                                        spread,
                                                                                                                50.dp.toPx(),
                                                                                                                50.dp.toPx(),
                                                                                                                frameworkPaint
                                                                                                        )
                                                                                        }
                                                                                }
                                                                        },
                                                        shape =
                                                                if (aiExpanded) aiPillShape
                                                                else CircleShape,
                                                        color = MaterialTheme.colorScheme.surface,
                                                        tonalElevation =
                                                                if (aiExpanded) 6.dp else 4.dp,
                                                        border =
                                                                BorderStroke(
                                                                        if (aiExpanded) 2.dp
                                                                        else 1.dp,
                                                                        if (aiExpanded) {
                                                                                if (isAiFullyExpanded
                                                                                ) {
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Brush
                                                                                                .linearGradient(
                                                                                                        colors =
                                                                                                                listOf(
                                                                                                                        Color(
                                                                                                                                0xFFE040FB
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF7C4DFF
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF448AFF
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF18FFFF
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFFE040FB
                                                                                                                        )
                                                                                                                ),
                                                                                                        start =
                                                                                                                androidx.compose
                                                                                                                        .ui
                                                                                                                        .geometry
                                                                                                                        .Offset(
                                                                                                                                x =
                                                                                                                                        (Math.cos(
                                                                                                                                                        Math.toRadians(
                                                                                                                                                                rotation.toDouble()
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                                .toFloat() +
                                                                                                                                                1f) /
                                                                                                                                                2f *
                                                                                                                                                1000f,
                                                                                                                                y =
                                                                                                                                        (Math.sin(
                                                                                                                                                        Math.toRadians(
                                                                                                                                                                rotation.toDouble()
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                                .toFloat() +
                                                                                                                                                1f) /
                                                                                                                                                2f *
                                                                                                                                                1000f
                                                                                                                        ),
                                                                                                        end =
                                                                                                                androidx.compose
                                                                                                                        .ui
                                                                                                                        .geometry
                                                                                                                        .Offset(
                                                                                                                                x =
                                                                                                                                        (Math.cos(
                                                                                                                                                        Math.toRadians(
                                                                                                                                                                rotation.toDouble() +
                                                                                                                                                                        180
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                                .toFloat() +
                                                                                                                                                1f) /
                                                                                                                                                2f *
                                                                                                                                                1000f,
                                                                                                                                y =
                                                                                                                                        (Math.sin(
                                                                                                                                                        Math.toRadians(
                                                                                                                                                                rotation.toDouble() +
                                                                                                                                                                        180
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                                .toFloat() +
                                                                                                                                                1f) /
                                                                                                                                                2f *
                                                                                                                                                1000f
                                                                                                                        )
                                                                                                )
                                                                                } else {
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Brush
                                                                                                .linearGradient(
                                                                                                        colors =
                                                                                                                listOf(
                                                                                                                        Color(
                                                                                                                                0xFFE040FB
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF7C4DFF
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF448AFF
                                                                                                                        ),
                                                                                                                        Color(
                                                                                                                                0xFF18FFFF
                                                                                                                        )
                                                                                                                )
                                                                                                )
                                                                                }
                                                                        } else {
                                                                                androidx.compose.ui
                                                                                        .graphics
                                                                                        .SolidColor(
                                                                                                MaterialTheme
                                                                                                        .colorScheme
                                                                                                        .surfaceVariant
                                                                                        )
                                                                        }
                                                                ),
                                                        onClick = {
                                                                if (!aiExpanded) aiExpanded = true
                                                        }
                                                ) {
                                                        Box(modifier = Modifier.fillMaxSize()) {
                                                                AnimatedContent(
                                                                        targetState = aiExpanded,
                                                                        transitionSpec = {
                                                                                fadeIn(
                                                                                        animationSpec =
                                                                                                tween(
                                                                                                        durationMillis =
                                                                                                                500,
                                                                                                        delayMillis =
                                                                                                                150
                                                                                                )
                                                                                ) togetherWith
                                                                                        fadeOut(
                                                                                                animationSpec =
                                                                                                        tween(
                                                                                                                durationMillis =
                                                                                                                        250
                                                                                                        )
                                                                                        )
                                                                        },
                                                                        label = "aiBarContent"
                                                                ) { isAiExpanded ->
                                                                        if (isAiExpanded) {
                                                                                // Full AI
                                                                                // Search
                                                                                // Bar
                                                                                TextField(
                                                                                        value =
                                                                                                aiSearchQuery,
                                                                                        onValueChange = {
                                                                                                aiSearchQuery =
                                                                                                        it
                                                                                        },
                                                                                        leadingIcon = {
                                                                                                Icon(
                                                                                                        Icons.Default
                                                                                                                .Search,
                                                                                                        contentDescription =
                                                                                                                "AI Search",
                                                                                                        tint =
                                                                                                                MaterialTheme
                                                                                                                        .colorScheme
                                                                                                                        .onSurfaceVariant,
                                                                                                        modifier =
                                                                                                                Modifier.size(
                                                                                                                        20.dp
                                                                                                                )
                                                                                                )
                                                                                        },
                                                                                        placeholder = {
                                                                                                Text(
                                                                                                        "Talk to YouTube",
                                                                                                        color =
                                                                                                                MaterialTheme
                                                                                                                        .colorScheme
                                                                                                                        .onSurfaceVariant
                                                                                                )
                                                                                        },
                                                                                        modifier =
                                                                                                Modifier.fillMaxSize()
                                                                                                        .clip(
                                                                                                                aiPillShape
                                                                                                        )
                                                                                                        .padding(
                                                                                                                horizontal =
                                                                                                                        6.dp
                                                                                                        )
                                                                                                        .focusRequester(
                                                                                                                aiFocusRequester
                                                                                                        ),
                                                                                        colors =
                                                                                                TextFieldDefaults
                                                                                                        .colors(
                                                                                                                focusedContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                unfocusedContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                disabledContainerColor =
                                                                                                                        Color.Transparent,
                                                                                                                focusedIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                                unfocusedIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                                disabledIndicatorColor =
                                                                                                                        Color.Transparent,
                                                                                                        ),
                                                                                        singleLine =
                                                                                                true,
                                                                                        trailingIcon = {
                                                                                                if (aiSearchQuery
                                                                                                                .isNotEmpty()
                                                                                                ) {
                                                                                                        IconButton(
                                                                                                                onClick = {
                                                                                                                        aiSearchQuery =
                                                                                                                                ""
                                                                                                                }
                                                                                                        ) {
                                                                                                                Icon(
                                                                                                                        Icons.Default
                                                                                                                                .Clear,
                                                                                                                        contentDescription =
                                                                                                                                "Clear",
                                                                                                                        tint =
                                                                                                                                MaterialTheme
                                                                                                                                        .colorScheme
                                                                                                                                        .onSurfaceVariant,
                                                                                                                        modifier =
                                                                                                                                Modifier.size(
                                                                                                                                        18.dp
                                                                                                                                )
                                                                                                                )
                                                                                                        }
                                                                                                }
                                                                                        }
                                                                                )
                                                                        } else {
                                                                                // Collapsed
                                                                                // AI Search
                                                                                // Button
                                                                                // Icon
                                                                                Box(
                                                                                        modifier =
                                                                                                Modifier.fillMaxSize(),
                                                                                        contentAlignment =
                                                                                                Alignment
                                                                                                        .Center
                                                                                ) {
                                                                                        Icon(
                                                                                                Icons.Filled
                                                                                                        .AutoAwesome,
                                                                                                contentDescription =
                                                                                                        "Switch to AI Search",
                                                                                                tint =
                                                                                                        MaterialTheme
                                                                                                                .colorScheme
                                                                                                                .onSurfaceVariant,
                                                                                                modifier =
                                                                                                        Modifier.size(
                                                                                                                18.dp
                                                                                                        )
                                                                                        )
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                },
                                actions = {},
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                titleContentColor =
                                                        MaterialTheme.colorScheme.onSurface
                                        )
                        )
                }
        ) { paddingValues ->
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
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
                modifier = Modifier.fillMaxSize().padding(24.dp),
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
fun SearchResults(searchQuery: String, onVideoClick: (String, String, String) -> Unit) {
        val sampleVideoUrl =
                "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

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
                                        val encodedUrl =
                                                URLEncoder.encode(
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

        Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
                // Thumbnail Section
                Box(contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                                model = thumbnailUrl,
                                contentDescription = "Thumbnail",
                                modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
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
                                        modifier =
                                                Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                        }
                }

                // Details Section
                Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.Top
                ) {
                        // Channel Avatar
                        AsyncImage(
                                model = avatarUrl,
                                contentDescription = "Avatar",
                                modifier =
                                        Modifier.size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color.Gray),
                                contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Text Info
                        Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = title,
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
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
                                onClick = { /* Show options menu */},
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
