@file:OptIn(ExperimentalMaterial3Api::class)

package com.linroid.pexels.screen.curated

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import com.linroid.pexels.api.model.Photo
import org.jetbrains.compose.resources.stringResource
import pexels.composeapp.generated.resources.Res
import pexels.composeapp.generated.resources.app_name
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
class CuratedScreen : Screen {
	@Composable
	override fun Content() {
		Scaffold(topBar = { PexelsAppBar() }) {
			val curatedModel = rememberScreenModel { CuratedModel() }
			val pullToRefreshState = rememberPullToRefreshState()
			LaunchedEffect(curatedModel.uiState) {
				if (curatedModel.uiState == CuratedModel.UiState.Refreshing) {
					pullToRefreshState.startRefresh()
				} else {
					pullToRefreshState.endRefresh()
				}
			}
			LaunchedEffect(pullToRefreshState.isRefreshing) {
				if (pullToRefreshState.isRefreshing) {
					curatedModel.refresh()
				}
			}
			BoxWithConstraints(
				Modifier.fillMaxSize()
					.padding(top = it.calculateTopPadding())
					.nestedScroll(pullToRefreshState.nestedScrollConnection)
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
					items(curatedModel.photos, key = { photo -> photo.id }) { photo ->
						PhotoItem(photo, staggeredGridStyle)
					}
					if (curatedModel.uiState == CuratedModel.UiState.Idle
						&& curatedModel.uiState != CuratedModel.UiState.End
						|| curatedModel.uiState == CuratedModel.UiState.LoadingMore
					) {
						item {
							LaunchedEffect(curatedModel.uiState) {
								curatedModel.loadMore()
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
				PullToRefreshContainer(
					modifier = Modifier.align(Alignment.TopCenter),
					state = pullToRefreshState,
				)
			}
		}
	}


}

@Composable
private fun PexelsAppBar() {
	Surface(shadowElevation = 4.dp) {
		TopAppBar(
			title = {
				Text(stringResource(Res.string.app_name))
			},
			colors = TopAppBarColors(
				containerColor = Color(0xFF07A080),
				scrolledContainerColor = Color.Unspecified,
				navigationIconContentColor = Color.White,
				titleContentColor = Color.White,
				actionIconContentColor = Color.White
			),
		)
	}
}

@Composable
private fun PhotoItem(photo: Photo, staggeredGridStyle: StaggeredGridStyle) {
	val density = LocalDensity.current
	Card(
		Modifier.padding(
			bottom = staggeredGridStyle.verticalMargin,
			start = staggeredGridStyle.horizontalMargin,
			end = staggeredGridStyle.horizontalMargin
		),
		shape = RoundedCornerShape(staggeredGridStyle.cardRadius)
	) {
		Column(Modifier.clickable { }) {
			BoxWithConstraints {
				val aspectRatio = photo.width.toFloat() / photo.height
				AsyncImage(
					modifier = Modifier.fillMaxWidth()
						.height(with(density) { (constraints.maxWidth / aspectRatio).toDp() }),
					model = photo.src.medium,
					contentScale = ContentScale.FillBounds,
					contentDescription = photo.alt,
				)
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
