package edu.virginia.cs4720.sgw5ne.finalproject.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Indigo700,
    onPrimary = Color.White,
    primaryContainer = Indigo100,
    onPrimaryContainer = Indigo900,
    secondary = Coral700,
    onSecondary = Color.White,
    secondaryContainer = Coral100,
    onSecondaryContainer = Coral900,
    background = Mist50,
    onBackground = Slate900,
    surface = Mist50,
    onSurface = Slate900,
    surfaceVariant = Mist200,
    onSurfaceVariant = Slate600,
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo200,
    onPrimary = Indigo900,
    primaryContainer = Indigo800,
    onPrimaryContainer = Indigo100,
    secondary = Coral200,
    onSecondary = Coral900,
    secondaryContainer = Coral800,
    onSecondaryContainer = Coral100,
    background = Night900,
    onBackground = Mist100,
    surface = Night900,
    onSurface = Mist100,
    surfaceVariant = Night700,
    onSurfaceVariant = Mist300,
)

@Composable
fun FinalprojectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Dynamic (wallpaper) color is intentionally off so the app keeps its own identity.
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
