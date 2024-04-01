package com.linroid.pexels

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.linroid.pexels.screen.feed.CuratedScreen
import com.linroid.pexels.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    Navigator(CuratedScreen()) {navigator->
        FadeTransition(navigator)
    }
}
