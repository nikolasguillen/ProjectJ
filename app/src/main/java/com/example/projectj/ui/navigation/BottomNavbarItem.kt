package com.example.projectj.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavbarItem(val screen: Screen, val activeIcon: ImageVector, val inactiveIcon: ImageVector) {
    object Home : BottomNavbarItem(
        screen = Screen.Home,
        activeIcon = Icons.Filled.CollectionsBookmark,
        inactiveIcon = Icons.Outlined.CollectionsBookmark
    )
}
