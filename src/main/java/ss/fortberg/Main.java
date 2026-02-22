package ss.fortberg;

import ss.fortberg.patcher.ElectronAppPatcher;
import ss.fortberg.server.FortGraphBergServer;
import ss.fortberg.ssdp.SsdpDiscovery;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FortGraphBergServer.startServer();
        ElectronAppPatcher.patchAll();
        SsdpDiscovery.start();
    }
}
