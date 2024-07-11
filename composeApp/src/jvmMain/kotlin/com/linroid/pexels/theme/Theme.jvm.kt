package com.linroid.pexels.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(isDark: Boolean) {
}

@Composable
actual fun provideColorScheme(isSystemDark: Boolean): ColorScheme {
	return if (isSystemDark) darkScheme else lightScheme
}