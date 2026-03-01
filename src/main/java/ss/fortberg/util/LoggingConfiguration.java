package ss.fortberg.util;

import java.io.IOException;
import java.util.logging.*;

public class LoggingConfiguration implements FBLogger {

    public static void init() throws IOException {
        Logger rootLogger = Logger.getLogger("");
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        rootLogger.setLevel(Level.INFO);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        FileHandler fileHandler = new FileHandler("%h/fb_%u.log", true);
        fileHandler.setLevel(Level.FINE);
        fileHandler.setFormatter(new SimpleFormatter());
        FileHandler fileErrHandler = new FileHandler("%h/fb_err_%u.log", true);
        fileErrHandler.setLevel(Level.SEVERE);
        fileErrHandler.setFormatter(new SimpleFormatter());
        log.addHandler(consoleHandler);
        log.addHandler(fileHandler);
        log.addHandler(fileErrHandler);
        log.setLevel(Level.FINE);
    }
}
