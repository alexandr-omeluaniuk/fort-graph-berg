package ss.fortberg;

import ss.fortberg.patcher.ElectronAppPatcher;
import ss.fortberg.server.FortGraphBergServer;
import ss.fortberg.ssdp.SsdpDiscovery;
import ss.fortberg.terminal.SmartX;
import ss.fortberg.util.Externalizator;
import ss.fortberg.util.LoggingConfiguration;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        LoggingConfiguration.init();
        FortGraphBergServer.startServer();
        ElectronAppPatcher.patchAll();
        SsdpDiscovery.start();
        if (Externalizator.getTerminalIp() != null) {
            SmartX.createNew(Externalizator.getTerminalIp());
            SmartX.getInstance().info();
        }
    }
}
