package com.linroid.pexels

import com.linroid.pexels.api.PexelsApi
import org.koin.dsl.module

val apiModule = module {
	single {
		PexelsApi(PEXELS_API_KEY)
	}
}

