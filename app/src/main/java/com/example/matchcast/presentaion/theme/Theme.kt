package com.example.matchcast.presentaion.theme


import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = EplPurple,
    secondary = EplNeonGreen,
    background = BackgroundLight,
    surface = SurfaceWhite,

    onPrimary = SurfaceWhite,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceWhite,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
)

/*
private val DarkColorScheme = darkColorScheme(
    primary = EplPurple,
    secondary = EplNeonGreen,
    background = BackgroundDark,
    surface = SurfaceDark,

    onPrimary = SurfaceWhite,
    onSecondary = TextPrimary,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = TextMutedDark,
)
*/

@Composable
fun MatchCastTheme(
    content: @Composable () -> Unit
) {
    val colorScheme =  LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = EplPurple.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EplTypography,
        content = content
    )
}
