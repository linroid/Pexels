@file:OptIn(ExperimentalMaterial3Api::class)

package com.linroid.pexels.screen.feed

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import coil3.compose.AsyncImage
import com.linroid.pexels.api.model.Photo
import com.linroid.pexels.components.BackHandler
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pexels.composeapp.generated.resources.Res
import pexels.composeapp.generated.resources.button_back

@Composable
fun PhotoViewer(
	photo: Photo,
	boundsInFeed: Rect,
	onExit: () -> Unit
) {
	var needTransition by remember { mutableStateOf(true) }
	val density = LocalDensity.current
	val coroutineScope = rememberCoroutineScope()
	val transitionProgress = remember { Animatable(if (needTransition) 0f else 1f) }
	BackHandler {
		needTransition = true
		coroutineScope.launch {
			transitionProgress.animateTo(0f)
			onExit()
		}
	}
	Scaffold(
		containerColor = Color.Transparent,
		topBar = {
			TopAppBar(
				title = {},
				navigationIcon = {
					IconButton(onClick = {
						needTransition = true
						coroutineScope.launch {
							transitionProgress.animateTo(0f)
							onExit()
						}
					}) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = stringResource(Res.string.button_back)
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
			)
		}) {
		BoxWithConstraints(
			Modifier.fillMaxSize()
			.background(Color.Black.copy(alpha = transitionProgress.value))
		) {
			val boxAspectRatio = constraints.maxWidth.toFloat() / constraints.maxHeight
			val photoAspectRatio = photo.width.toFloat() / photo.height
			val photoInitSize = remember {
				if (photoAspectRatio > boxAspectRatio) {
					Size(
						width = constraints.maxWidth.toFloat(),
						height = constraints.maxWidth / photoAspectRatio
					)
				} else {
					Size(
						width = constraints.maxHeight * photoAspectRatio,
						height = constraints.maxHeight.toFloat()
					)
				}
			}

			val photoInitOffset = remember {
				Offset(
					constraints.maxWidth / 2f - photoInitSize.width / 2f,
					constraints.maxHeight / 2f - photoInitSize.height / 2f,
				)
			}

			var isOriginalLoaded by remember { mutableStateOf(false) }

			LaunchedEffect(Unit) {
				if (needTransition) {
					transitionProgress.animateTo(1.0f) {}
					needTransition = false
				}
			}
			val scale = remember { Animatable(1f) }
			val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
			Box(
				Modifier.fillMaxSize()
					.pointerInput(Unit) {
						if (isOriginalLoaded) {
							detectTapGestures(onDoubleTap = {
								val fitScale = if (photoAspectRatio > boxAspectRatio) {
									constraints.maxHeight.toFloat() / photoInitSize.height
								} else {
									constraints.maxWidth.toFloat() / photoInitSize.width
								}
								coroutineScope.launch {
									scale.animateTo(if (scale.value < 1.0f || scale.value >= fitScale) 1.0f else fitScale)
								}
								coroutineScope.launch {
									offset.animateTo(Offset(0f, 0f))
								}
							})
						}
					}
					.pointerInput(Unit) {
						if (isOriginalLoaded) {
							detectTransformGestures { _, pan, zoom, _ ->
								coroutineScope.launch {
									scale.snapTo((scale.value * zoom).coerceIn(0.5f, 5f))
								}
								coroutineScope.launch {

									offset.snapTo(
										if (scale.value == 1f) Offset(
											0f,
											0f
										) else offset.value + pan
									)
								}
							}
						}
					}
			) {
				Box(Modifier
					.size(with(density) {
						if (needTransition) {
							DpSize(
								width = (boundsInFeed.width + transitionProgress.value * (photoInitSize.width - boundsInFeed.width)).toDp(),
								height = (boundsInFeed.height + transitionProgress.value * (photoInitSize.height - boundsInFeed.height)).toDp(),
							)
						} else {
							photoInitSize.toDpSize()
						}
					})
					.offset {
						if (needTransition) {
							IntOffset(
								x = (boundsInFeed.left + transitionProgress.value * (photoInitOffset.x - boundsInFeed.left)).toInt(),
								y = (boundsInFeed.top + transitionProgress.value * (photoInitOffset.y - boundsInFeed.top)).toInt(),
							)
						} else {
							IntOffset(photoInitOffset.x.toInt(), photoInitOffset.y.toInt())
						}
					}
					.graphicsLayer(
						scaleX = scale.value, scaleY = scale.value,
						translationX = offset.value.x, translationY = offset.value.y
					)
				) {
					if (!isOriginalLoaded) {
						AsyncImage(
							modifier = Modifier.fillMaxSize(),
							model = photo.src.medium,
							contentScale = ContentScale.FillBounds,
							contentDescription = photo.alt,
						)
					}
					AsyncImage(
						modifier = Modifier.fillMaxSize(),
						model = photo.src.original,
						contentScale = ContentScale.FillBounds,
						contentDescription = photo.alt,
						onState = { state ->
							if (state.painter != null) {
								isOriginalLoaded = true
							}
						}
					)
					if (!needTransition && !isOriginalLoaded) {
						CircularProgressIndicator(Modifier.align(Alignment.Center))
					}
				}
			}
		}
	}
}