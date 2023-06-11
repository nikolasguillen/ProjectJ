package com.example.projectj.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


// Material 3 color schemes
private val projectJDarkColorScheme = darkColorScheme(
    primary = projectJDarkPrimary,
    onPrimary = projectJDarkOnPrimary,
    primaryContainer = projectJDarkPrimaryContainer,
    onPrimaryContainer = projectJDarkOnPrimaryContainer,
    inversePrimary = projectJDarkPrimaryInverse,
    secondary = projectJDarkSecondary,
    onSecondary = projectJDarkOnSecondary,
    secondaryContainer = projectJDarkSecondaryContainer,
    onSecondaryContainer = projectJDarkOnSecondaryContainer,
    tertiary = projectJDarkTertiary,
    onTertiary = projectJDarkOnTertiary,
    tertiaryContainer = projectJDarkTertiaryContainer,
    onTertiaryContainer = projectJDarkOnTertiaryContainer,
    error = projectJDarkError,
    onError = projectJDarkOnError,
    errorContainer = projectJDarkErrorContainer,
    onErrorContainer = projectJDarkOnErrorContainer,
    background = projectJDarkBackground,
    onBackground = projectJDarkOnBackground,
    surface = projectJDarkSurface,
    onSurface = projectJDarkOnSurface,
    inverseSurface = projectJDarkInverseSurface,
    inverseOnSurface = projectJDarkInverseOnSurface,
    surfaceVariant = projectJDarkSurfaceVariant,
    onSurfaceVariant = projectJDarkOnSurfaceVariant,
    outline = projectJDarkOutline
)

private val projectJLightColorScheme = lightColorScheme(
    primary = projectJLightPrimary,
    onPrimary = projectJLightOnPrimary,
    primaryContainer = projectJLightPrimaryContainer,
    onPrimaryContainer = projectJLightOnPrimaryContainer,
    inversePrimary = projectJLightPrimaryInverse,
    secondary = projectJLightSecondary,
    onSecondary = projectJLightOnSecondary,
    secondaryContainer = projectJLightSecondaryContainer,
    onSecondaryContainer = projectJLightOnSecondaryContainer,
    tertiary = projectJLightTertiary,
    onTertiary = projectJLightOnTertiary,
    tertiaryContainer = projectJLightTertiaryContainer,
    onTertiaryContainer = projectJLightOnTertiaryContainer,
    error = projectJLightError,
    onError = projectJLightOnError,
    errorContainer = projectJLightErrorContainer,
    onErrorContainer = projectJLightOnErrorContainer,
    background = projectJLightBackground,
    onBackground = projectJLightOnBackground,
    surface = projectJLightSurface,
    onSurface = projectJLightOnSurface,
    inverseSurface = projectJLightInverseSurface,
    inverseOnSurface = projectJLightInverseOnSurface,
    surfaceVariant = projectJLightSurfaceVariant,
    onSurfaceVariant = projectJLightOnSurfaceVariant,
    outline = projectJLightOutline
)

@Composable
fun ProjectJTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val projectJColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> projectJDarkColorScheme
        else -> projectJLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = projectJColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = projectJColorScheme,
        typography = projectJTypography,
        shapes = shapes,
        content = content
    )
}