package org.vortex.zapi.client.api.coreUtil;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.vortex.zapi.client.SettingsHolder;
import org.vortex.zapi.client.ZapiClient;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class UtilHandler implements HttpHandler {

    // Utility states
    private static final Map<String, Boolean> utilityStates = new HashMap<>() {{
        put("fly", false);
        put("nofall", false);
    }};

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
        // Construct response as a valid JSON string
        StringBuilder response = new StringBuilder("{");
        utilityStates.forEach((key, value) ->
                response.append("\"").append(key).append("\": ").append(value).append(", ")
        );

        // Remove trailing comma and space, close the JSON object
        if (response.length() > 1) {
            response.setLength(response.length() - 2);
        }
        response.append("}");

        sendResponse(exchange, 200, response.toString());
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        // Read the body to determine what to toggle
        String requestBody = new String(exchange.getRequestBody().readAllBytes()).trim();

        if (utilityStates.containsKey(requestBody)) {
            // Toggle the requested utility state
            boolean currentState = utilityStates.get(requestBody);
            utilityStates.put(requestBody, !currentState);
            if ("nofall".equals(requestBody)) {
                SettingsHolder.nofall = !currentState;
            }
            if ("fly".equals(requestBody)) {
                SettingsHolder.fly = !currentState;
            }
            sendResponse(exchange, 200, String.format("{\"%s\": %b}", requestBody, !currentState));
        } else {
            sendResponse(exchange, 400, "Invalid utility state requested");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
