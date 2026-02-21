package ss.fortberg.httpclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.readValue
import ss.fortberg.httpclient.model.AuthRequest
import ss.fortberg.httpclient.model.AuthResponse
import ss.fortberg.util.Externalizator
import ss.fortberg.util.FBLogger
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

    fun printControlCheck() {
        val response: String? = withAuth<String>("CONTROL_TAPE",null)
        log.info("Control check printing result: $response")
    }

    private inline fun <reified V> withAuth(intent: String, payload: Any?): V? {
        if (accessToken == null) {
            accessToken = request<AuthResponse>("AUTH", AuthRequest(pin = Externalizator.getPin()))?.token
        }
        return request<V>(intent, payload)
    }

    private inline fun <reified V> request(intent: String, payload: Any?): V? =
        try {
            val payloadStr = payload?.let { objectMapper.writeValueAsString(it) } ?: ""
            log.info("Terminal request:\n$payloadStr")
            val request = HttpRequest.newBuilder().uri(URI.create(rootUrl))
                .header("Content-Type", "application/json")
                .header("INTENT_OPERATION_TYPE", intent)
                .POST(HttpRequest.BodyPublishers.ofString(payloadStr))
            if (accessToken != null) {
                request.header("Auth-token", accessToken)
            }
            val response = client.send(request.build(), HttpResponse.BodyHandlers.ofString())
            log.info("Terminal response [${response.statusCode()}]:\n${response.body()}")
            objectMapper.readValue<V>(response.body())
        } catch (e: Exception) {
            log.log(Level.SEVERE, "Terminal request error: ${e.message}", e)
            accessToken = null
            null
        }
}