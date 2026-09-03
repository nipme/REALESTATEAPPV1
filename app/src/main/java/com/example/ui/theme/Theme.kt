package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val ElegantDarkColorScheme = darkColorScheme(
    primary = ElegantDarkPrimary,
    onPrimary = ElegantDarkOnPrimary,
    primaryContainer = ElegantDarkPrimaryContainer,
    onPrimaryContainer = ElegantDarkOnPrimaryContainer,
    secondary = ElegantDarkSecondary,
    onSecondary = ElegantDarkOnSecondary,
    background = ElegantDarkBg,
    onBackground = ElegantDarkText,
    surface = ElegantDarkSurface,
    onSurface = ElegantDarkText,
    surfaceVariant = ElegantDarkSurfaceVariant,
    onSurfaceVariant = ElegantDarkTextSecondary,
    outline = ElegantDarkOutline,
    error = FavoriteRed,
    onError = DeepViolet
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
        else -> ElegantDarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

