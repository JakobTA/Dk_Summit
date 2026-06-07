package com.example.mk8dxplaylist.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFFE53935),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8B0000),
    secondary = Color(0xFFFFB300),
    onSecondary = Color.Black,
    background = Color(0xFF1A1A2E),
    surface = Color(0xFF16213E),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}
