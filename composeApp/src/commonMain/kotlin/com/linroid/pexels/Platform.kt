@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels

internal expect object Platform {
    fun openUrl(url: String)
}