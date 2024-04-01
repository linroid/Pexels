@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels.storage

import kotlinx.serialization.Serializable

interface ObjectStore<T : @Serializable Any> {
	suspend fun set(value: T)
	suspend fun delete()
	suspend fun get(): T?
}

expect inline fun <reified T : @Serializable Any> objectStoreOf(key: String): ObjectStore<T>