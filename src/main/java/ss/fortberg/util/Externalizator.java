package ss.fortberg.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Externalizator implements FBLogger {

    private static final Properties props = new Properties();

    static {
        try {
            final var propsFile = new File("fort.berg.properties");
            if (propsFile.exists()) {
                props.load(new FileInputStream(propsFile));
                log.info("External configuration was loaded successfully");
            } else {
                log.warning("External configuration is not exist:" + propsFile.getAbsolutePath());
            }
        } catch (Exception e) {
            log.severe("External configuration problem: " + e.getMessage());
        }
    }

    public static String getPin() {
        return props.getProperty("pin", "1234");
    }

    public static String getMoySkladHomeDir() {
        return props.getProperty("moy.sklad.home.dir");
    }
}
