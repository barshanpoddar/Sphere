package com.sphere.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.sphere.app.ui.screens.SearchScreen

@Composable
fun SphereApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Search,
        Screen.Subscription,
        Screen.Music
    )

    // Hide BottomBar on Splash Screen and Player Screen
    val showBottomBar = currentRoute != Screen.Splash.route && currentRoute?.startsWith("player") == false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                com.sphere.app.ui.screens.SplashScreen(onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) {
                com.sphere.app.ui.screens.HomeScreen(
                    onVideoClick = { videoUrl: String, title: String, channel: String ->
                        navController.navigate(Screen.Player.createRoute(videoUrl, title, channel))
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }
            composable(Screen.Explore.route) { com.sphere.app.ui.screens.ExploreScreen() }
            composable(Screen.Search.route) { 
                SearchScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onVideoClick = { videoUrl: String, title: String, channel: String ->
                        navController.navigate(Screen.Player.createRoute(videoUrl, title, channel))
                    }
                )
            }
            composable(Screen.Subscription.route) { com.sphere.app.ui.screens.SubscriptionScreen() }
            composable(Screen.Music.route) { com.sphere.app.ui.screens.MusicScreen() }
            composable(Screen.Profile.route) { com.sphere.app.ui.screens.ProfileScreen() }
            composable(
                route = Screen.Player.route,
                arguments = listOf(
                    navArgument("videoUrl") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("channel") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                val title = backStackEntry.arguments?.getString("title") ?: ""
                val channel = backStackEntry.arguments?.getString("channel") ?: ""

                // Decode arguments if strictly necessary, but compose often handles basic string args well.
                // However, URLs usually need decoding if they were encoded.
                val decodedUrl = try { URLDecoder.decode(videoUrl, StandardCharsets.UTF_8.toString()) } catch (_: Exception) { videoUrl }

                com.sphere.app.ui.screens.PlayerScreen(
                    videoUrl = decodedUrl,
                    title = title,
                    channelName = channel
                )
            }
        }
    }
}
