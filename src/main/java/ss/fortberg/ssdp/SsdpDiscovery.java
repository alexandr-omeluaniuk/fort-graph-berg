package ss.fortberg.ssdp;

import io.resourcepool.ssdp.client.SsdpClient;
import io.resourcepool.ssdp.model.SsdpRequest;
import ss.fortberg.util.FBLogger;

public class SsdpDiscovery implements FBLogger {

    public static void start() {
        log.info("Starting...");
        final var ssdpClient = SsdpClient.create();
        ssdpClient.discoverServices(
            SsdpRequest.builder().serviceType("ikassa-smartx").build(),
            new TerminalDiscoveryListener()
        );
    }
}
