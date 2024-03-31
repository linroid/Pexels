package com.linroid.pexels.api

import io.ktor.client.plugins.ClientRequestException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PexelsApiTest {
	private val testApiKey = "ScVpu45vZekZ0ncguaBKiwyJU0MBMRf4xMzJTkyoZwyEDL0L4LNTcVoK"
	@Test
	fun invalidApiKey() = runTest {
		val api = PexelsApi("")
		assertFailsWith<ClientRequestException> {
			api.curatedPhotos()
		}
	}
	@Test
	fun curatedPhotos() = runTest {
		val api = PexelsApi(testApiKey)
		val pagination = api.curatedPhotos()
		assertTrue { pagination.photos.isNotEmpty() }
	}

	@Test
	fun curatedPhotosWithPerPage() = runTest {
		val api = PexelsApi(testApiKey)
		val pagination = api.curatedPhotos(perPage = 32)
		assertTrue { pagination.photos.size == 32 }
	}
}