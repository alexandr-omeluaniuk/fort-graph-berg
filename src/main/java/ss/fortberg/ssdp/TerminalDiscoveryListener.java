package ss.fortberg.ssdp;

import io.resourcepool.ssdp.model.DiscoveryListener;
import io.resourcepool.ssdp.model.SsdpService;
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement;
import ss.fortberg.terminal.SmartX;
import ss.fortberg.util.FBLogger;

import java.util.logging.Level;

public class TerminalDiscoveryListener implements DiscoveryListener, FBLogger {

    @Override
    public void onServiceDiscovered(SsdpService service) {
        log.info("Discovered terminal location: " + service.getLocation());
        try {
            SmartX.createNew(service.getLocation());
        } catch (Exception e) {
            log.log(Level.SEVERE, "SmartX initialization failed", e);
        }
    }

    @Override
    public void onServiceAnnouncement(SsdpServiceAnnouncement announcement) {
        log.info("Discovered announcement terminal location: " + announcement.getLocation());
    }

    @Override
    public void onFailed(Exception e) {
        log.log(Level.SEVERE, "Discovering error: " + e.getMessage(), e);
    }
}
