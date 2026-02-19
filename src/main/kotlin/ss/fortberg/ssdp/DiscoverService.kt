package ss.fortberg.ssdp

import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement

object DiscoverService {

    fun discover() {
        val ssdpClient = SsdpClient.create()
        ssdpClient.discoverServices(
            SsdpRequest.builder().serviceType("ikassa-smartx").build(),
            object : DiscoveryListener {
                override fun onServiceDiscovered(service: SsdpService) {
                    println("SERVICE")
                    println(service)
                }

                override fun onServiceAnnouncement(a: SsdpServiceAnnouncement) {
                    println("A")
                    println(a)
                }

                override fun onFailed(err: Exception) {
                    println("ERROR")
                    println(err)
                }
            }
        )
    }
}