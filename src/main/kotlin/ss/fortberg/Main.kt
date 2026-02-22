package ss.fortberg

import ss.fortberg.patcher.KassaPatcher
import ss.fortberg.ssdp.DiscoverService

fun main(args: Array<String>) {
    KassaPatcher.patchAll()
    DiscoverService.discover()
}