package com.example.projectj.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.projectj.ui.discover_screen.DiscoverViewModel
import com.example.projectj.ui.navigation.ProjectJBottomNavigationBar
import com.example.projectj.ui.navigation.ProjectJNavigationActions
import com.example.projectj.ui.navigation.ProjectJNavigationRail
import com.example.projectj.ui.navigation.ProjectJRoute
import com.example.projectj.ui.navigation.ProjectJTopLevelDestination
import com.example.projectj.ui.utils.DevicePosture
import com.example.projectj.ui.utils.ProjectJContentType
import com.example.projectj.ui.utils.ProjectJNavigationContentPosition
import com.example.projectj.ui.utils.ProjectJNavigationType
import com.example.projectj.ui.utils.isBookPosture
import com.example.projectj.ui.utils.isSeparating

@Composable
fun ProjectJApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>
) {

    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: ProjectJNavigationType
    val contentType: ProjectJContentType

    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ProjectJNavigationType.BOTTOM_NAVIGATION
            contentType = ProjectJContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = ProjectJNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ProjectJContentType.DUAL_PANE
            } else {
                ProjectJContentType.SINGLE_PANE
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType =
                ProjectJNavigationType.NAVIGATION_RAIL
            contentType = ProjectJContentType.DUAL_PANE
        }

        else -> {
            navigationType = ProjectJNavigationType.BOTTOM_NAVIGATION
            contentType = ProjectJContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            ProjectJNavigationContentPosition.TOP
        }

        WindowHeightSizeClass.Medium -> {
            ProjectJNavigationContentPosition.CENTER
        }

        WindowHeightSizeClass.Expanded -> {
            ProjectJNavigationContentPosition.BOTTOM
        }

        else -> {
            ProjectJNavigationContentPosition.TOP
        }
    }

    ProjectJNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition
    )
}

@Composable
private fun ProjectJNavigationWrapper(
    navigationType: ProjectJNavigationType,
    contentType: ProjectJContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ProjectJNavigationContentPosition
) {

    val navController = rememberNavController()
    val navigationActions = remember(navController) { ProjectJNavigationActions(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ProjectJRoute.FAVORITES

    ProjectJAppContent(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        navController = navController,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = navigationActions::navigateTo
    )
}

@Composable
fun ProjectJAppContent(
    modifier: Modifier = Modifier,
    navigationType: ProjectJNavigationType,
    contentType: ProjectJContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ProjectJNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (ProjectJTopLevelDestination) -> Unit
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == ProjectJNavigationType.NAVIGATION_RAIL) {
            ProjectJNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ProjectJNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(visible = navigationType == ProjectJNavigationType.BOTTOM_NAVIGATION) {
                ProjectJBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun ProjectJNavHost(
    navController: NavHostController,
    contentType: ProjectJContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: ProjectJNavigationType,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ProjectJRoute.DISCOVER
    ) {
        composable(ProjectJRoute.FAVORITES) {

        }

        composable(ProjectJRoute.DISCOVER) {
            val viewModel = hiltViewModel<DiscoverViewModel>()

            ProjectJMangaListScreen(
                viewModel = viewModel,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                navigateToDetail = { _, _ -> }
            )
        }

        composable(ProjectJRoute.ACCOUNT) {

        }

        composable(ProjectJRoute.UPDATED) {

        }
    }

}