package com.spicerub.ratio.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = Paprika,
    onPrimary = TextOnPrimary,
    primaryContainer = PaprikaLight,
    onPrimaryContainer = TextOnPrimary,
    secondary = Cardamom,
    onSecondary = TextOnPrimary,
    secondaryContainer = TurmericCream,
    onSecondaryContainer = TextPrimary,
    tertiary = Terracotta,
    onTertiary = TextOnPrimary,
    background = BackgroundTop,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfacePanel,
    onSurfaceVariant = TextSecondary,
    outline = GoldRim,
    outlineVariant = SurfaceMuted,
    error = PaprikaDark,
    onError = TextOnPrimary
)

private val DarkScheme = darkColorScheme(
    primary = PaprikaDarkMode,
    onPrimary = TextPrimaryDark,
    primaryContainer = PaprikaDark,
    onPrimaryContainer = TextPrimaryDark,
    secondary = CardamomLight,
    onSecondary = RoastedBottom,
    secondaryContainer = SecondaryDarkMode,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = Terracotta,
    onTertiary = RoastedBottom,
    background = RoastedTop,
    onBackground = TextPrimaryDark,
    surface = RoastedSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = RoastedPanel,
    onSurfaceVariant = TextSecondaryDark,
    outline = GoldRim,
    outlineVariant = RoastedDivider,
    error = PaprikaDarkMode,
    onError = TextPrimaryDark
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}
