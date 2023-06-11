package com.example.projectj.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectj.ui.navigation.BottomNavbarItem
import com.google.android.material.elevation.SurfaceColors

@Composable
fun CustomNavigationRail(
    currentRoute: String,
    items: List<BottomNavbarItem>,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    NavigationRail(
        windowInsets = WindowInsets(
            bottom = 64.dp,
            top = 16.dp,
            left = 8.dp,
            right = 8.dp
        ),
        containerColor = Color(SurfaceColors.SURFACE_2.getColor(context))
    ) {
        Spacer(modifier = Modifier.weight(1f))
        items.forEach { item ->
            NavigationRailItem(
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
                },
                alwaysShowLabel = currentRoute == item.screen.route
            )
        }
    }
}