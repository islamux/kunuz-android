package com.islamux.kunuz.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat

private val KunuzColorScheme = lightColorScheme(
    primary = KunuzPrimary,
    onPrimary = KunuzSurface,
    primaryContainer = KunuzPrimaryDark,
    secondary = KunuzAccent,
    background = KunuzBackground,
    onBackground = KunuzText,
    surface = KunuzSurface,
    onSurface = KunuzText,
    outline = KunuzBorder
)

@Composable
fun KunuzTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = KunuzPrimaryDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = KunuzColorScheme,
            typography = MaterialTheme.typography.copy(bodyLarge = KunuzTypography),
            content = content
        )
    }
}