package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DeepNavyDarkColorScheme = darkColorScheme(
    primary = RoyalBlueLight,
    onPrimary = DeepNavyBg,
    primaryContainer = RoyalBlueContainerDark,
    onPrimaryContainer = RoyalBlueOnContainerDark,
    secondary = Color(0xFF93C5FD),
    onSecondary = DeepNavyBg,
    background = DeepNavyBg,
    onBackground = DeepNavyText,
    surface = DeepNavySurface,
    onSurface = DeepNavyText,
    surfaceVariant = DeepNavySurfaceVariant,
    onSurfaceVariant = DeepNavyTextSecondary,
    outline = DeepNavyOutline,
    error = FavoriteRed,
    onError = Color.White
)

private val DeepNavyLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    background = LightBg,
    onBackground = LightText,
    surface = LightSurface,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightOutline,
    error = FavoriteRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DeepNavyDarkColorScheme
        else -> DeepNavyLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
