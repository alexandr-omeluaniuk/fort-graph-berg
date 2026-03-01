module fortberg.main {
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires jdk.httpserver;
    requires java.net.http;
    exports ss.fortberg.server.model;
    exports ss.fortberg.storage.model;
    exports ss.fortberg.terminal.model;
}