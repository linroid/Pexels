package com.linroid.pexels.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
internal actual fun SystemAppearance(isDarkTheme: Boolean) {
    LaunchedEffect(isDarkTheme) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (isDarkTheme) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}

@Composable
internal actual fun provideColorScheme(isSystemDark: Boolean): ColorScheme {
    return if (isSystemDark) darkScheme else lightScheme
}