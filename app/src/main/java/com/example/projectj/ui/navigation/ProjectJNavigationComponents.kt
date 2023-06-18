package com.example.projectj.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projectj.ui.utils.ProjectJNavigationContentPosition

@Composable
fun ProjectJNavigationRail(
    selectedDestination: String,
    navigationContentPosition: ProjectJNavigationContentPosition,
    navigateToTopLevelDestination: (ProjectJTopLevelDestination) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 16.dp),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {

        val alignment by remember {
            mutableStateOf(
                when (navigationContentPosition) {
                    ProjectJNavigationContentPosition.TOP -> Alignment.Top
                    ProjectJNavigationContentPosition.CENTER -> Alignment.CenterVertically
                    ProjectJNavigationContentPosition.BOTTOM -> Alignment.Bottom
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp, alignment = alignment)
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { projectJDestination ->

                val icon by remember(selectedDestination) {
                    mutableStateOf(
                        if (selectedDestination == projectJDestination.route) {
                            projectJDestination.selectedIcon
                        } else {
                            projectJDestination.unselectedIcon
                        }
                    )
                }

                NavigationRailItem(
                    selected = selectedDestination == projectJDestination.route,
                    onClick = {
                        navigateToTopLevelDestination(projectJDestination)
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(id = projectJDestination.iconTextId)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectJBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (ProjectJTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { projectJDestination ->

            val icon by remember(selectedDestination) {
                mutableStateOf(
                    if (selectedDestination == projectJDestination.route) {
                        projectJDestination.selectedIcon
                    } else {
                        projectJDestination.unselectedIcon
                    }
                )
            }

            NavigationBarItem(
                selected = selectedDestination == projectJDestination.route,
                onClick = { navigateToTopLevelDestination(projectJDestination) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(id = projectJDestination.iconTextId)
                    )
                }
            )
        }
    }
}