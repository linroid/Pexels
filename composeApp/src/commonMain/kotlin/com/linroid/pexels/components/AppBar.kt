@file:OptIn(ExperimentalMaterial3Api::class)

package com.linroid.pexels.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import pexels.composeapp.generated.resources.Res
import pexels.composeapp.generated.resources.app_name


@Composable
fun PexelsAppBar() {
	Surface(shadowElevation = 4.dp) {
		TopAppBar(
			colors = TopAppBarDefaults.topAppBarColors(
				containerColor = Color(0xFF07A080),
				titleContentColor = Color.White,
				actionIconContentColor = Color.White,
			),
			title = {
				Text(stringResource(Res.string.app_name))
			},
		)
	}
}