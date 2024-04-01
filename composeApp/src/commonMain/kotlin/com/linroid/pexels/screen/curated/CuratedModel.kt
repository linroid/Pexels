package com.linroid.pexels.screen.curated

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.linroid.pexels.api.PexelsApi
import com.linroid.pexels.api.model.Photo
import io.github.aakira.napier.Napier
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CuratedModel : ScreenModel, KoinComponent {

	private val coroutineScope = MainScope()
	private val api: PexelsApi by inject()
	private var currentPage: Int = 1

	private val photoIds = mutableSetOf<Int>()
	private var _photos = mutableStateListOf<Photo>()
	val photos: List<Photo> get() = _photos.toList()

	private var _state by mutableStateOf(UiState.Idle)
	val uiState: UiState get() = _state

	init {
		refresh()
	}

	override fun onDispose() {
		super.onDispose()
		coroutineScope.cancel()
	}

	fun refresh() {
		if (_state == UiState.Refreshing) {
			return
		}
		Napier.i("refresh")
		currentPage = 1
		_state = UiState.Refreshing
		coroutineScope.launch {
			try {
				val pagination = api.curatedPhotos(currentPage)
				photoIds.clear()
				_photos.clear()
				_photos.addAll(pagination.photos)
				photoIds.addAll(pagination.photos.map { it.id }.toSet())
				_state = UiState.Idle
			} catch (error: Exception) {
				Napier.e("Unable to refresh", error)
				_state = UiState.Error
			}
		}
	}

	fun loadMore() {
		if (_state == UiState.LoadingMore) {
			return
		}
		currentPage++
		Napier.i { "loading page $currentPage" }
		_state = UiState.LoadingMore
		coroutineScope.launch {
			try {
				val pagination = api.curatedPhotos(currentPage)
				if (pagination.photos.isEmpty()) {
					_state = UiState.End
				} else {
					/**
					 * Pexels server may return duplicate photos, we need to filter them
					 */
					_photos.addAll(pagination.photos.filter { !photoIds.contains(it.id) })
					photoIds.addAll(pagination.photos.map { it.id }.toSet())
					_state = UiState.Idle
				}
			} catch (error: Exception) {
				Napier.e("Unable to refresh", error)
				_state = UiState.Error
			}
		}
	}

	enum class UiState {
		Refreshing,
		LoadingMore,
		End,
		Idle,
		Error
	}
}