package com.linroid.pexels

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.linroid.pexels.storage.objectStoreOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.dsl.module
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@Serializable
data class Person(
	@SerialName("name")
	val name: String,
	@SerialName("age")
	val age: Int,
)

class ObjectStoreTest {
	@BeforeTest
	fun setup() {
		val androidModule = module {
			single<Context> {
				InstrumentationRegistry.getInstrumentation().context
			}
		}
	}

	@Test
	fun storeAndGet() = runTest {
		val store = objectStoreOf<Person>("storeAndGet")
		val person = Person("Foo", 100)
		store.set(person)
		assertEquals(person.name, store.get()?.name)
	}

	@Test
	fun storeAndDelete() = runTest {
		val store = objectStoreOf<Person>("storeAndGet")
		val person = Person("Foo", 100)
		store.set(person)
		assertEquals(person.name, store.get()?.name)
		store.delete()
		assertNull(store.get())
	}
}