package com.example.finalexamapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Tracker : Screen("tracker", "Tracker", Icons.Filled.DirectionsRun)
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Dashboard)
    object Achievements : Screen("achievements", "Achievements", Icons.Filled.EmojiEvents)
}
