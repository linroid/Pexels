package com.linroid.pexels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import pexels.composeapp.generated.resources.*
import com.linroid.pexels.theme.AppTheme
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun App() = AppTheme {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.hello_world),
            fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
