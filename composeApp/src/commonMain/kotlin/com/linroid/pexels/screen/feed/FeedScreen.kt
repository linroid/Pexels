@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.linroid.pexels.screen.feed

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.linroid.pexels.api.model.Photo
import com.linroid.pexels.components.PexelsAppBar
import kotlin.math.absoluteValue

val avatarColors =
	arrayOf(
		Color(0xFF006064),
		Color(0xFF01579B),
		Color(0xFF004D40),
		Color(0xFF1B5E20),
		Color(0xFF827717),
		Color(0xFF616161),
		Color(0xFFBF360C),
		Color(0xFF455A64),
		Color(0xFF512DA8),
	)

@Composable
fun HomeScreen(
	sharedTransitionScope: SharedTransitionScope,
	animatedContentScope: AnimatedContentScope,
	viewModel: HomeViewModel = viewModel { HomeViewModel() },
	onViewPhoto: (photo: Photo) -> Unit,
) {
	Scaffold(topBar = { PexelsAppBar() }) {
		PullToRefreshBox(
			isRefreshing = viewModel.uiState == HomeViewModel.UiState.Refreshing,
			onRefresh = viewModel::refresh
		) {
			BoxWithConstraints(
				Modifier.fillMaxSize()
					.padding(top = it.calculateTopPadding())
			) {
				val density = LocalDensity.current
				val staggeredGridStyle by derivedStateOf {
					with(density) {
						StaggeredGridStyle.fromWidth(constraints.maxWidth.toDp())
					}
				}
				LazyVerticalStaggeredGrid(
					columns = StaggeredGridCells.Fixed(staggeredGridStyle.columnCount),
					contentPadding = PaddingValues(
						horizontal = staggeredGridStyle.horizontalPadding,
						vertical = staggeredGridStyle.verticalPadding
					)
				) {
					items(viewModel.photos, key = { photo -> photo.id }) { photo ->
						PhotoItem(
							sharedTransitionScope,
							animatedContentScope,
							photo,
							staggeredGridStyle
						) {
							onViewPhoto(photo)
						}
					}
					if (viewModel.uiState == HomeViewModel.UiState.Idle
						&& viewModel.uiState != HomeViewModel.UiState.End
						|| viewModel.uiState == HomeViewModel.UiState.LoadingMore
					) {
						item {
							LaunchedEffect(viewModel.uiState) {
								viewModel.loadMore()
							}
							Card(
								Modifier.padding(bottom = 8.dp, start = 2.dp, end = 2.dp),
								shape = RoundedCornerShape(4.dp)
							) {
								Box(Modifier.fillMaxSize().padding(vertical = 56.dp)) {
									CircularProgressIndicator(Modifier.align(Alignment.Center))
								}
							}
						}
					}
				}
			}
		}

	}
}

@Composable
private fun PhotoItem(
	sharedTransitionScope: SharedTransitionScope,
	animatedContentScope: AnimatedContentScope,
	photo: Photo,
	staggeredGridStyle: StaggeredGridStyle,
	onViewPhoto: () -> Unit
) {
	val density = LocalDensity.current
	Card(
		Modifier.padding(
			bottom = staggeredGridStyle.verticalMargin,
			start = staggeredGridStyle.horizontalMargin,
			end = staggeredGridStyle.horizontalMargin
		),
		shape = RoundedCornerShape(staggeredGridStyle.cardRadius)
	) {
		Column(Modifier.clickable {
			onViewPhoto()
		}) {
			BoxWithConstraints {
				val photoHeight = constraints.maxWidth / (photo.width.toFloat() / photo.height)
				with(sharedTransitionScope) {
					AsyncImage(
						modifier = Modifier.fillMaxWidth()
							.height(with(density) { photoHeight.toDp() })
							.sharedElement(
								sharedTransitionScope.rememberSharedContentState(key = "photo-${photo.id}"),
								animatedVisibilityScope = animatedContentScope
							),
						model = photo.src.medium,
						contentScale = ContentScale.FillBounds,
						contentDescription = photo.alt,
					)
				}
			}
			Row(
				Modifier.padding(staggeredGridStyle.photographerPadding),
				verticalAlignment = Alignment.CenterVertically
			) {
				Box(
					Modifier.size(staggeredGridStyle.avatarSize)
						.clip(CircleShape)
						.background(avatarColors[(photo.photographer.hashCode() % avatarColors.size).absoluteValue])
				) {
					Text(
						photo.photographer.firstOrNull()?.toString()?.uppercase() ?: "",
						modifier = Modifier.align(Alignment.Center),
						fontSize = staggeredGridStyle.photographerFontSize,
						color = Color.White
					)
				}
				Spacer(Modifier.width(staggeredGridStyle.photographerPadding))
				Text(
					text = photo.photographer,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					fontSize = staggeredGridStyle.photographerFontSize,
				)
			}
		}
	}
}
