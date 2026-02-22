package ss.fortberg.httpserver

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import ss.fortberg.util.FBLogger
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets


class FortBergServer : FBLogger {

    fun startServer() {
        val server = HttpServer.create(InetSocketAddress(19879), 0)
        server.createContext("/") { exchange: HttpExchange ->
            exchange.sendResponseHeaders(200, 0)
            exchange.requestBody.use { r ->
                val payload = String(r.readAllBytes(), StandardCharsets.UTF_8)
                log.info("Kassa data:\n$payload")
            }
        }
        server.start()
        log.info("Server was run on port 19879...")
    }
}