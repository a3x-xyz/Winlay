package com.winlay.a3x.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = WinlayPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = WinlayPrimaryDark,
    onPrimaryContainer = WinlayAccentLight,

    secondary = WinlayAccent,
    onSecondary = WinlayNeutral900,
    secondaryContainer = WinlayAccentDark,
    onSecondaryContainer = WinlayNeutral900,

    tertiary = WinlayNeutral300,
    onTertiary = WinlayNeutral900,
    tertiaryContainer = WinlayNeutral800,
    onTertiaryContainer = WinlayNeutral100,

    background = WinlaySurfaceDark,
    onBackground = WinlayNeutral100,

    surface = WinlaySurfaceVariantDark,
    onSurface = WinlayNeutral100,
    surfaceVariant = WinlayNeutral800,
    onSurfaceVariant = WinlayNeutral200,

    error = WinlayError,
    onError = Color.White,
    errorContainer = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = WinlayNeutral600,
    outlineVariant = WinlayNeutral700
)

private val LightColorScheme = lightColorScheme(
    primary = WinlayPrimary,
    onPrimary = Color.White,
    primaryContainer = WinlayPrimaryLight,
    onPrimaryContainer = WinlayPrimaryDark,

    secondary = WinlayAccent,
    onSecondary = WinlayNeutral900,
    secondaryContainer = WinlayAccentLight,
    onSecondaryContainer = WinlayAccentDark,

    tertiary = WinlayNeutral700,
    onTertiary = Color.White,
    tertiaryContainer = WinlayNeutral300,
    onTertiaryContainer = WinlayNeutral800,

    background = WinlaySurfaceLight,
    onBackground = WinlayNeutral900,

    surface = WinlaySurfaceVariantLight,
    onSurface = WinlayNeutral900,
    surfaceVariant = WinlayNeutral200,
    onSurfaceVariant = WinlayNeutral800,

    error = WinlayError,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = WinlayNeutral400,
    outlineVariant = WinlayNeutral300
)

@Composable
fun WinlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalSpacing provides Dimensions()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = WinlayTypography,
            content = content
        )
    }
}

data class Dimensions(
    val tiny: androidx.compose.ui.unit.Dp = 4.dp,
    val small: androidx.compose.ui.unit.Dp = 8.dp,
    val medium: androidx.compose.ui.unit.Dp = 16.dp,
    val large: androidx.compose.ui.unit.Dp = 24.dp,
    val xlarge: androidx.compose.ui.unit.Dp = 32.dp,
    val xxlarge: androidx.compose.ui.unit.Dp = 48.dp
)

val LocalSpacing = compositionLocalOf { Dimensions() }
