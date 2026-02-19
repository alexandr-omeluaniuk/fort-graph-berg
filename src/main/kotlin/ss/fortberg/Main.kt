package ss.fortberg

import ss.fortberg.ssdp.DiscoverService

fun main(args: Array<String>) {
    DiscoverService.discover()
    Thread.sleep(20000)
}