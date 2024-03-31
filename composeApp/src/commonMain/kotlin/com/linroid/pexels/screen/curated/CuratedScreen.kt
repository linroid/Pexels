@file:OptIn(ExperimentalMaterial3Api::class)

package com.linroid.pexels.screen.curated

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import com.linroid.pexels.api.model.Photo
import com.linroid.pexels.theme.parseColor
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.stringResource
import pexels.composeapp.generated.resources.Res
import pexels.composeapp.generated.resources.app_name

class CuratedScreen : Screen {
	@Composable
	override fun Content() {
		Scaffold(topBar = { PexelsAppBar() }) {
			val pullToRefreshState = rememberPullToRefreshState()
			val curatedModel = rememberScreenModel { CuratedModel(pullToRefreshState) }
			BoxWithConstraints(
				Modifier.fillMaxSize()
					.padding(top = it.calculateTopPadding())
					.nestedScroll(pullToRefreshState.nestedScrollConnection)
			) {
				val density = LocalDensity.current
				val columnsCount by derivedStateOf {
					with(density) {
						val windowWidth = constraints.maxWidth.toDp()
						if (windowWidth < 200.dp) {
							1
						} else if (windowWidth < 400.dp) {
							2
						} else {
							3
						}
					}
				}
				LazyVerticalStaggeredGrid(
					columns = StaggeredGridCells.Fixed(columnsCount),
					contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
				) {
					items(curatedModel.photos.toList(), key = { photo -> photo.id }) { photo ->
						PhotoItem(photo)
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
private fun PhotoItem(photo: Photo) {
	val density = LocalDensity.current
	Card(
		Modifier.padding(bottom = 8.dp, start = 2.dp, end = 2.dp),
		shape = RoundedCornerShape(4.dp)
	) {
		Column {
			BoxWithConstraints {
				val aspectRatio = photo.width.toFloat() / photo.height
				AsyncImage(
					model = photo.src.medium,
					contentDescription = null,
					modifier = Modifier.fillMaxWidth()
						.height(with(density) { (constraints.maxWidth / aspectRatio).toDp() })
						.background(parseColor(photo.avgColor)),
					onState = { state ->
						Napier.i("State changes to $state")
					})
			}
			Row(
				Modifier.padding(4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				AsyncImage(
					model = photo.photographerUrl,
					contentDescription = photo.photographer,
					modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.Red)
				)
				Spacer(Modifier.width(4.dp))
				Text(
					text = photo.photographer,
					style = MaterialTheme.typography.labelSmall,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}
