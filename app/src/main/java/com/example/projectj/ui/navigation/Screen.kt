package com.example.projectj.ui.navigation

import androidx.annotation.StringRes
import com.example.projectj.R

sealed class Screen(val route: String, @StringRes val titleResId: Int) {
    object Login : Screen(route = "login", titleResId = R.string.destination_login)
    object Home : Screen(route = "favorites", titleResId = R.string.destination_favorites)
}
