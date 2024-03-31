@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels

import kotlinx.browser.window

internal actual object Platform {
    actual fun openUrl(url: String) {
        window.open(url)
    }
}