package ss.fortberg.server;

import com.sun.net.httpserver.HttpServer;
import ss.fortberg.server.model.Assortment;
import ss.fortberg.server.model.RetailInfo;
import ss.fortberg.storage.DataStorage;
import ss.fortberg.util.FBLogger;
import ss.fortberg.util.JsonUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class FortGraphBergServer implements FBLogger {

    public static void startServer() throws IOException {
        final var server = HttpServer.create(new InetSocketAddress(19879), 0);
        server.createContext("/retail", (exchange) -> {
            exchange.sendResponseHeaders(200, 0);
            try (final var is = exchange.getRequestBody()) {
                final var payload = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                log.info("MoySklad retail:\n" + payload);
                final RetailInfo retailInfo = JsonUtils.objectMapper.readValue(payload, RetailInfo.class);
                DataStorage.getInstance().setCashiers(retailInfo);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Process request from Electron App failed", e);
            }
            exchange.close();
        });
        server.createContext("/sale", (exchange) -> {
            exchange.sendResponseHeaders(200, 0);
            try (final var is = exchange.getRequestBody()) {
                final var payload = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                log.info("MoySklad sale:\n" + payload);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Process request from Electron App failed", e);
            }
            exchange.close();
        });
        server.createContext("/products", (exchange) -> {
            exchange.sendResponseHeaders(200, 0);
            try (final var is = exchange.getRequestBody()) {
                final var payload = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                log.info("MoySklad products:\n" + payload);
                final Assortment assortment = JsonUtils.objectMapper.readValue(payload, Assortment.class);
                DataStorage.getInstance().setProducts(assortment);
            } catch (IOException e) {
                log.log(Level.SEVERE, "Process request from Electron App failed", e);
            }
            exchange.close();
        });
        server.start();
        log.info("Server was run on port 19879...");
    }
}
