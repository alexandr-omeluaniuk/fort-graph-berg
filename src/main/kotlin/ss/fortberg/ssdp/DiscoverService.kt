package ss.fortberg.ssdp

import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement
import ss.fortberg.httpclient.KassaTerminal
import ss.fortberg.util.FBLogger
import java.util.logging.Level

/**
 * Search for iKassa location and start of HTTP client initialization
 */
object DiscoverService : FBLogger {

    lateinit var terminal: KassaTerminal

    fun discover() {
        log.info("Starting...")
        val ssdpClient = SsdpClient.create()
        ssdpClient.discoverServices(
            SsdpRequest.builder().serviceType("ikassa-smartx").build(),
            object : DiscoveryListener {
                override fun onServiceDiscovered(service: SsdpService) {
                    log.info("Discovered terminal location: " + service.location)
                    terminal = KassaTerminal(service.location)
                    terminal.getOtherInfo()
                }
                override fun onServiceAnnouncement(a: SsdpServiceAnnouncement) {
                    log.info("Discovered announcement terminal location: " + a.location)
                }
                override fun onFailed(err: Exception) {
                    log.log(Level.SEVERE, "Discovering error: ${err.message}", err)
                }
            }
        )
    }
}