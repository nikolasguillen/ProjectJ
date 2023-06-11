package com.example.projectj.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectj.R
import com.example.projectj.ui.navigation.Screen

@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToFavorites: () -> Unit,
    closeDrawer: () -> Unit,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier
) {
    val modifierB =
        if (!isExpandedScreen) modifier.width(LocalConfiguration.current.screenWidthDp.dp * (2/3)) else modifier


    ModalDrawerSheet(
        drawerTonalElevation = 3.dp,
        modifier = modifierB
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            NavigationDrawerItem(
                label = {
                    Text(
                        stringResource(id = R.string.destination_favorites),
                        fontWeight = FontWeight.Medium
                    )
                },
                icon = { Icon(Icons.Filled.Home, null) },
                selected = currentRoute == Screen.Home.route,
                onClick = { navigateToFavorites(); closeDrawer() },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
}