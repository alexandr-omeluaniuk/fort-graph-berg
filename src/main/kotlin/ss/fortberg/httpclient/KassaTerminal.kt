package ss.fortberg.httpclient

import java.net.http.HttpClient

/**
 * Communicator with terminal over HTTP protocol
 */
class KassaTerminal(
    private val location: String
) {
    private val rootUrl = "$location/v1"

    private val client: HttpClient = initClient()

    private fun initClient(): HttpClient {
        val cl = HttpClient.newBuilder().build()
        return cl
    }
}