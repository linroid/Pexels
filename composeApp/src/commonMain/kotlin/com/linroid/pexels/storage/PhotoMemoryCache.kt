package com.linroid.pexels.storage

import androidx.collection.LruCache
import com.linroid.pexels.api.model.Photo

/**
 * Workaround to get photo data structure from the Int id argument.
 * The serialized data is not supported by compose navigation
 */
object PhotoMemoryCache {
	private val cache = LruCache<Int, Photo>(10)

	fun getById(id: Int): Photo {
		return cache[id] ?: throw IllegalStateException("No cached photo found for $id")
	}

	fun put(photo: Photo) {
		cache.put(photo.id, photo)
	}
}