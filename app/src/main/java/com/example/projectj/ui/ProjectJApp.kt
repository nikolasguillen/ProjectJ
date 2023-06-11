package com.example.projectj.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
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
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                ProjectJNavigationType.NAVIGATION_RAIL
            } else {
                ProjectJNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
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
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            ProjectJNavigationContentPosition.CENTER
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()

}