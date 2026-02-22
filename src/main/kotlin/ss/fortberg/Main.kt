package ss.fortberg

import ss.fortberg.httpserver.FortBergServer
import ss.fortberg.patcher.KassaPatcher
import ss.fortberg.ssdp.DiscoverService

fun main(args: Array<String>) {
    FortBergServer.init()
    KassaPatcher.patchAll()
    DiscoverService.discover()
}