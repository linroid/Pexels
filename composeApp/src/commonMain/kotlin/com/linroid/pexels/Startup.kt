package com.linroid.pexels

import org.koin.core.context.startKoin

object Startup {
	fun run() {
		startKoin {
			modules(apiModule)
		}
	}
}