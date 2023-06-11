package com.example.projectj.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projectj.ui.navigation.Screen

@Composable
fun NavHostController.getCurrentRoute(): String {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route ?: Screen.Home.route
}