package ss.fortberg.patcher;

import ss.fortberg.util.Externalizator;
import ss.fortberg.util.FBLogger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ElectronAppPatcher implements FBLogger {

    private static final String TERMINAL_SCRIPT_REF = "<script src=\"./ikassa.js\"></script>";
    private static final String MAKE_REQUEST_REF = "makeRequest(data) {";
    private static final String TERMINAL_RESPONSE_INTERCEPTOR_REF = "iKassaTerminal.intercept(data, response.clone());\n\t\t\t\t";
    private static final String RETURN_RESPONSE_REF = "return response;";

    public static void patchAll() throws IOException {
        final var homeDir = Externalizator.getMoySkladHomeDir();
        if (homeDir != null && !homeDir.isBlank()) {
            patchIndexHtml(homeDir);
            patchTerminalJs(homeDir);
            patchRenderer(homeDir);
        }
    }

    private static void patchIndexHtml(String homeDir) throws IOException {
        final var indexHtml = new File(homeDir, "/resources/app/index.html");
        final var content = Files.readString(indexHtml.toPath());
        if (content.contains(TERMINAL_SCRIPT_REF)) {
            log.fine("index.html already patched... Skip...");
        } else {
            final var headCloseIdx = content.indexOf("</head>");
            final var newContent = content.substring(0, headCloseIdx) + TERMINAL_SCRIPT_REF + content.substring(headCloseIdx);
            Files.writeString(indexHtml.toPath(), newContent);
            log.info("index.html successfully patched...");
        }
    }

    private static void patchTerminalJs(String homeDir) throws IOException {
        final var jsFile = new File(homeDir, "/resources/app/ikassa.js");
        final var is = ElectronAppPatcher.class.getResourceAsStream("/patch/ikassa.js");
        final var content = new String(
            is.readAllBytes(),
            StandardCharsets.UTF_8
        );
        is.close();
        if (!jsFile.exists() || jsFile.length() != content.length()) {
            Files.writeString(jsFile.toPath(), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("'ikassa.js' successfully patched...");
        } else {
            log.fine("ikassa.js already patched... Skip...");
        }
    }

    private static void patchRenderer(String homeDir) throws IOException {
        final var rendererFile = new File(homeDir, "/resources/app/renderer.js");
        final var content = Files.readString(rendererFile.toPath());
        if (content.contains(TERMINAL_RESPONSE_INTERCEPTOR_REF)) {
            log.fine("renderer.js already patched... Skip...");
        } else {
            final var idx = content.indexOf(MAKE_REQUEST_REF);
            final var secondPart = content.substring(idx);
            final var idx2 = idx + secondPart.indexOf(RETURN_RESPONSE_REF);
            // insert in the middle
            final var newContent = content.substring(0, idx2) + TERMINAL_RESPONSE_INTERCEPTOR_REF + content.substring(idx2);
            Files.writeString(rendererFile.toPath(), newContent);
            log.info("renderer.js successfully patched...");
        }
    }
}
