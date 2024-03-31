@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal actual object Platform {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: throw IllegalArgumentException("Illegal url: $url")
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}