package com.example.projectj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.example.projectj.ui.ProjectJApp
import com.example.projectj.ui.theme.ProjectJTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            ProjectJTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                val displayFeatures = calculateDisplayFeatures(activity = this)

                ProjectJApp(
                    navController = navController,
                    windowSize = windowSize,
                    displayFeatures = displayFeatures
                )
            }
        }
    }
}