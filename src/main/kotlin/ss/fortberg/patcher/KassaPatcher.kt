package ss.fortberg.patcher

import ss.fortberg.util.Externalizator
import ss.fortberg.util.FBLogger
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption

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
        val indexHtml = File(homeDir, "/resources/app/index.html")
        val content = Files.readString(indexHtml.toPath())
        if (content.contains(TERMINAL_SCRIPT_REF)) {
            log.info("index.html already patched... Skip...")
        } else {
            val headCloseIdx = content.indexOf("</head>")
            val newContent = content.substring(0, headCloseIdx) + TERMINAL_SCRIPT_REF + content.substring(headCloseIdx)
            Files.writeString(indexHtml.toPath(), newContent)
            log.info("index.html successfully patched...")
        }
    }

    private fun patchTerminalJs() {
        val jsFile = File(homeDir, "/resources/app/ikassa.js")
        val content = String(
            KassaPatcher::class.javaClass.getResourceAsStream("/patch/ikassa.js")!!.readAllBytes(),
            StandardCharsets.UTF_8
        )
        if (!jsFile.exists() || jsFile.length() != content.length.toLong()) {
            Files.writeString(jsFile.toPath(), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            log.info("'ikassa.js' successfully patched...")
        } else {
            log.info("ikassa.js already patched... Skip...")
        }
    }

    private fun patchRenderer() {

    }
}

private const val TERMINAL_SCRIPT_REF = "<script src=\"./ikassa.js\"></script>"