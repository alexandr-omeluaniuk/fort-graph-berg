package ss.fortberg.patcher

import ss.fortberg.util.Externalizator
import ss.fortberg.util.FBLogger
import java.io.File
import java.io.FileNotFoundException

/**
 * Patched MoySklad application to intercept requests
 */
object KassaPatcher : FBLogger {

    private val homeDir = File(Externalizator.getMoySkladHomeDir()).also {
        if (!it.exists()) {
            throw FileNotFoundException("Moy Sklad home dir is not exist: ${it.absolutePath}")
        }
    }

    fun patchAll() {
        patchIndexHtml()
        patchTerminalJs()
        patchRenderer()
    }

    private fun patchIndexHtml() {

    }

    private fun patchTerminalJs() {

    }

    private fun patchRenderer() {

    }
}