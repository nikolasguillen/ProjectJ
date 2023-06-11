package com.example.projectj.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.projectj.ui.navigation.BottomNavbarItem

@Composable
fun CustomNavigationBar(
    currentRoute: String,
    items: List<BottomNavbarItem>,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = { onNavigate(item.screen.route) },
                label = {
                    Text(
                        text = stringResource(id = item.screen.titleResId),
                        fontWeight = if (currentRoute == item.screen.route) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.screen.route) item.activeIcon else item.inactiveIcon,
                        contentDescription = item.screen.route
                    )
                }
            )
        }
    }
}