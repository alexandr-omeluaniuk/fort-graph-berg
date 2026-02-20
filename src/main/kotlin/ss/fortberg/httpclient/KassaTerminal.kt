package ss.fortberg.httpclient

import ss.fortberg.FBLogger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.logging.Level

/**
 * Communicator with terminal over HTTP protocol
 */
class KassaTerminal(
    private val location: String
) : FBLogger {
    private val rootUrl = "$location/v1"

    private var accessToken: String? = null

    private val client: HttpClient = HttpClient.newBuilder().build()

    private fun request(payload: String): String? =
        try {
            log.info("Terminal request:\n$payload")
            val request = HttpRequest.newBuilder().uri(URI.create(rootUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            log.info("Terminal response [${response.statusCode()}]:\n${response.body()}")
            response.body()
        } catch (e: Exception) {
            log.log(Level.SEVERE, "Terminal request error: ${e.message}", e)
            null
        }
}