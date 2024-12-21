package org.vortex.zapi.client.api.key;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.Clipboard;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.vortex.zapi.client.SettingsHolder;
import org.vortex.zapi.client.ZapiClient;
import org.vortex.zapi.client.mixin.TitleScreenMixin;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class KeyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (!ZapiClient.useAuth) {
            sendResponse(exchange, 200, "{\"text\":\"Authentication is disabled.\"}");
            return;
        }
        if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            sendResponse(exchange, 405, "Method Not Allowed");
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // Example: Process POST request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes());



        if (SettingsHolder.key.equals(requestBody)) {
            sendResponse(exchange, 200, "{}");
            return;
        }
        sendResponse(exchange, 404, "{}");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}