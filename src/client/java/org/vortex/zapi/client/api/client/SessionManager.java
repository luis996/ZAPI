package org.vortex.zapi.client.api.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import org.vortex.zapi.client.SettingsHolder;
import org.vortex.zapi.client.ZapiClient;
import org.vortex.zapi.client.mixin.MinecraftAccessor;
import org.vortex.mcu.api.Utils;

import java.io.IOException;
import java.io.OutputStream;

public class SessionManager implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (ZapiClient.useAuth) {
            var authHeader = exchange.getRequestHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.equals(SettingsHolder.key)) {
                sendResponse(exchange, 403, "Unauthorized.");
                return; // Terminate further execution for unauthorized requests
            }
        }
        if ("GET".equalsIgnoreCase(method)) {
            handleGet(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        var client = MinecraftClient.getInstance();
        String sessionJson = Utils.sessionToJson(client.getSession());
        sendResponse(exchange, 200, sessionJson);
    }
    private void handlePost(HttpExchange exchange) throws IOException {
        var client = (MinecraftAccessor) MinecraftClient.getInstance();
        String sessionJson = new String(exchange.getRequestBody().readAllBytes());
        Session session = Utils.sessionFromJson(sessionJson);
        client.setSession(session);
        sendResponse(exchange, 200, Utils.sessionToJson(session));
    }



    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
