@file:OptIn(ExperimentalMaterial3Api::class)

package com.linroid.pexels.screen.curated

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import com.linroid.pexels.api.PexelsApi
import com.linroid.pexels.api.model.Photo
import io.github.aakira.napier.Napier
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CuratedModel(
	private val pullToRefreshState: PullToRefreshState
) : ScreenModel, KoinComponent {

	private var currentPage: Int = 1
	private val api: PexelsApi by inject()
	private val coroutineScope = MainScope()
	val photos = mutableStateListOf<Photo>()

	private var _isLoading = mutableStateOf(false)
	val isLoading: State<Boolean> = _isLoading

	init {
		refresh()
	}

	override fun onDispose() {
		super.onDispose()
		coroutineScope.cancel()
	}

	fun refresh() {
		Napier.i("refresh")
		currentPage = 1
		_isLoading.value = true
		pullToRefreshState.startRefresh()
		coroutineScope.launch {
			try {
				val pagination = api.curatedPhotos(currentPage)
				photos.clear()
				photos.addAll(pagination.photos)
			} catch (error: Exception) {
				Napier.e("Unable to refresh", error)
			} finally {
				pullToRefreshState.endRefresh()
			}
		}
	}
}