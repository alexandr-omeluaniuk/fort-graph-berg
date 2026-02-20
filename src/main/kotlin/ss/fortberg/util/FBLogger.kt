package ss.fortberg.util

import java.util.logging.Logger

interface FBLogger {

    val log: Logger
        get() = Logger.getLogger(this::class.java.getName())
}