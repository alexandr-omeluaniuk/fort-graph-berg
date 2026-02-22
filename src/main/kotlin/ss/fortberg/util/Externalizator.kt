package ss.fortberg.util

import java.io.File
import java.io.FileInputStream
import java.util.*

object Externalizator : FBLogger {

    private val props = Properties()

    init {
        val propsFile = File("fort.berg.properties")
        if (propsFile.exists()) {
            props.load(FileInputStream(propsFile))
            log.info("External configuration was loaded successfully")
        } else {
            log.warning("External configuration is not exist: ${propsFile.absolutePath}")
        }
    }

    fun getPin(): String = props.getProperty("pin", "1234")

    fun getMoySkladHomeDir() = props.getProperty("moy.sklad.home.dir")
        ?: throw IllegalArgumentException("'moy.sklad.home.dir' property is required")
}