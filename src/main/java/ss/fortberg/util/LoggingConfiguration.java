package ss.fortberg.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggingConfiguration implements FBLogger {

    public static void init() throws IOException {
        Logger rootLogger = Logger.getLogger("");
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        FileHandler fileHandler = new FileHandler("%h/fb_%u.log", true);
        fileHandler.setLevel(Level.FINE);
        fileHandler.setFormatter(new SimpleFormatter());
        log.addHandler(consoleHandler);
        log.addHandler(fileHandler);
        log.setLevel(Level.FINE);
    }
}
