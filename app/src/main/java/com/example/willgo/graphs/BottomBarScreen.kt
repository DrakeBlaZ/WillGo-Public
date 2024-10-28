package com.example.willgo.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "Home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Location: BottomBarScreen(
        route = "Location",
        title = "Location",
        icon = Icons.Default.LocationOn
    )

    object Profile: BottomBarScreen(
        route = "Profile",
        title = "Profile",
        icon = Icons.Default.Person
    )
}