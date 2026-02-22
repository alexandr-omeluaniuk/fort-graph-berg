package ss.fortberg.terminal;

import ss.fortberg.terminal.model.AuthRequest;
import ss.fortberg.terminal.model.AuthResponse;
import ss.fortberg.util.Externalizator;
import ss.fortberg.util.FBLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;

import static ss.fortberg.util.JsonUtils.objectMapper;

public class SmartX implements FBLogger {

    private static SmartX current = null;

    private final String rootUrl;

    private String accessToken;

    private final HttpClient client;

    public static void createNew(String location) throws IOException, InterruptedException {
        current = new SmartX(location);
    }

    public static SmartX getInstance() {
        return current;
    }

    private SmartX(String location) throws IOException, InterruptedException {
        rootUrl = location;
        client = HttpClient.newBuilder().build();
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
            log.info("Terminal request:\n$payloadStr");
            final var request = HttpRequest.newBuilder().uri(URI.create(rootUrl + "/v1"))
                .header("Content-Type", "application/json")
                .header("INTENT_OPERATION_TYPE", intent)
                .POST(HttpRequest.BodyPublishers.ofString(payloadStr));
            if (accessToken != null) {
                request.header("Auth-token", accessToken);
            }
            final var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
            log.info("Terminal response [${response.statusCode()}]:\n${response.body()}");
            if (responseType == String.class) {
                return (T) response.body();
            } else {
                return objectMapper.readValue(response.body(), responseType);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Terminal request error: ${e.message}", e);
            return null;
        }
    }
}
