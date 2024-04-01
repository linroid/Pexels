@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels.storage

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.serialization.Serializable
import net.harawata.appdirs.AppDirsFactory
import okio.Path.Companion.toPath
import java.io.File

val storageDir = AppDirsFactory.getInstance().getUserDataDir("Pexels", "1.0", "linroid")!!

class JvmObjectStore<T : @Serializable Any>(private val kstore: KStore<T>) : ObjectStore<T> {

	init {
		if (!File(storageDir).exists()) {
			File(storageDir).mkdirs()
		}
	}

	override suspend fun set(value: T) {
		kstore.set(value)
	}

	override suspend fun delete() {
		kstore.delete()
	}

	override suspend fun get(): T? {
		return kstore.get()
	}
}

actual inline fun <reified T : @Serializable Any> objectStoreOf(key: String): ObjectStore<T> {
	return JvmObjectStore(storeOf("$storageDir/${key}.json".toPath()))
}