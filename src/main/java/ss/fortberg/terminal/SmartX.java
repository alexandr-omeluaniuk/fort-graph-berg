package ss.fortberg.terminal;

import ss.fortberg.server.model.SaleRequest;
import ss.fortberg.storage.DataStorage;
import ss.fortberg.terminal.model.AuthRequest;
import ss.fortberg.terminal.model.AuthResponse;
import ss.fortberg.terminal.model.Sale;
import ss.fortberg.terminal.model.SaleItem;
import ss.fortberg.util.Externalizator;
import ss.fortberg.util.FBLogger;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.logging.Level;

import static ss.fortberg.util.JsonUtils.objectMapper;

public class SmartX implements FBLogger {

    private static SmartX current = null;

    private final String rootUrl;

    private String accessToken;

    private final HttpClient client;

    public static void createNew(String location) throws IOException, InterruptedException {
        current = new SmartX(location);
        log.info("Terminal created for address [" + location + "]");
    }

    public static SmartX getInstance() {
        return current;
    }

    private SmartX(String location) throws IOException, InterruptedException {
        rootUrl = location;
        client = HttpClient.newBuilder().build();
    }

    public void info() {
        String info = withAuth("OTHER_INFO", null, String.class);
        log.info("Terminal info: " + info);
    }

    public void sale(SaleRequest request) {
        final var ds = DataStorage.getInstance();
        final var cashier = ds.getCashier(request.owner().meta().id());
        final var cash = new BigDecimal(request.cashSum() / 100).setScale(2, RoundingMode.HALF_UP);
        final var cashless = new BigDecimal(request.noCashSum() / 100).setScale(2, RoundingMode.HALF_UP);
        final var sale = new Sale(
            cashier == null ? "Кассир" : cashier.lastname(),
            request.positions().stream().map(pos -> {
                final var product = ds.getProduct(pos.assortment().meta().id());
                if (product == null) {
                    return null;
                } else {
                    final var price = BigDecimal.valueOf(pos.price() / 100).setScale(2, RoundingMode.HALF_UP);
                    return new SaleItem(
                        product.name(),
                        product.code(),
                        price,
                        new BigDecimal(pos.quantity()),
                        true,
                        new BigDecimal(pos.discount())
                    );
                }
            }).filter(Objects::nonNull).toList(),
            cash,
            cashless,
            new BigDecimal(0),
            new BigDecimal(0),
            "",
            ""
        );
        log.info("Sale request: " + sale);
        final var response = withAuth("SALE", sale, String.class);
        log.info("Sale response: " + response);
    }

    private <T> T withAuth(String intent, Object payload, Class<T> responseType) {
        if (accessToken == null) {
            accessToken = request(
                "AUTH",
                new AuthRequest(Externalizator.getPin(), true , true),
                AuthResponse.class
            ).token();
        }
        return request(intent, payload, responseType);
    }

    private <T> T request(String intent, Object payload, Class<T> responseType) {
        try {
            String payloadStr;
            if (payload != null) {
                payloadStr = objectMapper.writeValueAsString(payload);
            } else {
                payloadStr = "";
            }
            log.fine("Terminal request:\n$payloadStr");
            final var request = HttpRequest.newBuilder().uri(URI.create(rootUrl + "/v1"))
                .header("Content-Type", "application/json")
                .header("INTENT_OPERATION_TYPE", intent)
                .POST(HttpRequest.BodyPublishers.ofString(payloadStr));
            if (accessToken != null) {
                request.header("Auth-token", accessToken);
            }
            final var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401) {
                accessToken = null;
                return withAuth(intent, payload, responseType);
            }
            log.fine("Terminal response [${response.statusCode()}]:\n${response.body()}");
            if (responseType == String.class) {
                return (T) response.body();
            } else {
                return objectMapper.readValue(response.body(), responseType);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Terminal request error: " + e.getMessage(), e);
            return null;
        }
    }
}
