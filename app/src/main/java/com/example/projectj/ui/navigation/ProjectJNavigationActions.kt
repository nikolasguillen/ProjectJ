package com.example.projectj.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.projectj.R

object ProjectJRoute {
    const val FAVORITES = "favorites"
    const val DISCOVER = "discover"
    const val ACCOUNT = "account"
    const val UPDATED = "updated"
}

data class ProjectJTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class ProjectJNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: ProjectJTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    ProjectJTopLevelDestination(
        route = ProjectJRoute.FAVORITES,
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Default.FavoriteBorder,
        iconTextId = R.string.tab_favorites
    ),
    ProjectJTopLevelDestination(
        route = ProjectJRoute.DISCOVER,
        selectedIcon = Icons.Default.NewReleases,
        unselectedIcon = Icons.Outlined.NewReleases,
        iconTextId = R.string.tab_discover
    ),
    ProjectJTopLevelDestination(
        route = ProjectJRoute.ACCOUNT,
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
        iconTextId = R.string.tab_account
    ),
    ProjectJTopLevelDestination(
        route = ProjectJRoute.UPDATED,
        selectedIcon = Icons.Default.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        iconTextId = R.string.tab_updated
    )

)