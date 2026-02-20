package ss.fortberg.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
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

    private val objectMapper = ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)

    private val rootUrl = "$location/v1"

    private var accessToken: String? = null

    private val client: HttpClient = HttpClient.newBuilder().build()

    private inline fun <reified V> request(payload: Any): V? =
        try {
            val payloadStr = objectMapper.writeValueAsString(payload)
            log.info("Terminal request:\n$payloadStr")
            val request = HttpRequest.newBuilder().uri(URI.create(rootUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payloadStr))
            if (accessToken != null) {
                request.header("", accessToken)
            }
            val response = client.send(request.build(), HttpResponse.BodyHandlers.ofString())
            log.info("Terminal response [${response.statusCode()}]:\n${response.body()}")
            objectMapper.readValue<V>(response.body())
        } catch (e: Exception) {
            log.log(Level.SEVERE, "Terminal request error: ${e.message}", e)
            null
        }
}