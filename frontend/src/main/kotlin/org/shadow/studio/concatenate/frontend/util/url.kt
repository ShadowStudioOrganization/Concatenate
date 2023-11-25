package org.shadow.studio.concatenate.frontend.util

import androidx.compose.runtime.Composable
import java.awt.Desktop
import java.io.IOException
import java.net.URI

fun openUrl(url: String) {
    val uri: URI = URI(url)
    if (Desktop.isDesktopSupported()) {
        val desktop: Desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}