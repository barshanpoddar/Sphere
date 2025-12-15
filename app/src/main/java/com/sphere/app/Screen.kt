package com.sphere.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Splash : Screen("splash", "Splash", null)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    object Subscription : Screen("subscription", "Subscription", Icons.Default.Subscriptions)
    object Profile : Screen("profile", "You", Icons.Default.AccountCircle)
    object Player : Screen("player/{videoUrl}/{title}/{channel}", "Player", null) {
        fun createRoute(videoUrl: String, title: String, channel: String): String {
            val encodedVideoUrl = URLEncoder.encode(videoUrl, StandardCharsets.UTF_8.toString())
            val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
            val encodedChannel = URLEncoder.encode(channel, StandardCharsets.UTF_8.toString())
            return "player/$encodedVideoUrl/$encodedTitle/$encodedChannel"
        }
    }
}
