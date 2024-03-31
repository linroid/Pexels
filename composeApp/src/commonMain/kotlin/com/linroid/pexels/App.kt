package com.linroid.pexels

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.linroid.pexels.screen.curated.CuratedScreen
import com.linroid.pexels.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    Navigator(CuratedScreen())
}
