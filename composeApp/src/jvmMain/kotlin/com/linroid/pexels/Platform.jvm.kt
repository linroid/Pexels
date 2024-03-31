@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.linroid.pexels

import java.awt.Desktop
import java.net.URI

internal actual object Platform {
    actual fun openUrl(url: String) {
        val uri = URI.create(url)
        Desktop.getDesktop().browse(uri)
    }
}