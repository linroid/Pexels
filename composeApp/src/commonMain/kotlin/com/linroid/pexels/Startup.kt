package com.linroid.pexels

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

object Startup {
	fun run() {
		Napier.base(DebugAntilog())
		startKoin {
			modules(apiModule)
		}
	}
}