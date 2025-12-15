package com.sphere.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Splash : Screen("splash", "Splash", null)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    object Subscription : Screen("subscription", "Subscription", Icons.Default.Subscriptions)
    object Profile : Screen("profile", "You", Icons.Default.AccountCircle)
}
