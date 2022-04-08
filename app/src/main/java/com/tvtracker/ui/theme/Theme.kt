package com.tvtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White

private val DarkColorPalette = darkColors(
        primary = MidGrey,
        primaryVariant = Purple700,
        secondary = Red,
        background = DarkGrey,
        surface = Black,
        onPrimary = White,
        onSecondary = Black,
        onBackground = Yellow,
        onSurface = Yellow
)

private val LightColorPalette = lightColors(
        primary = LightBlue,
        primaryVariant = LighterBlue,
        secondary = Red,
        background = Blue,
        surface = Black,
        onPrimary = Lime,
        onSecondary = Black,
        onBackground = Lime,
        onSurface = Lime,
)

@Composable
fun TvTrackerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}