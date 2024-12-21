package org.vortex.zapi.client;

import com.sun.net.httpserver.HttpServer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vortex.zapi.client.api.client.SessionManager;
import org.vortex.zapi.client.api.key.KeyHandler;
import org.vortex.zapi.client.api.coreUtil.UtilHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ZapiClient implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("ZAPI");
    public static HttpServer server;

    // useAuth boolean. Only set it to false in development environments.
    public static boolean useAuth = true;
    public static Text titleScreenText;
    static {
        try {
            server = HttpServer.create(new InetSocketAddress(25566), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onInitializeClient() {
        server.createContext("/api/v1/key", new KeyHandler());
        server.createContext("/api/v1/util", new UtilHandler());
        server.createContext("/api/v1/client/session", new SessionManager());
        server.setExecutor(null);
        server.start();

        if (!useAuth) {
            LOGGER.info("[ZAPI] Unsecure mode enabled. Please consult your ZAPI version source if you're not in a development environment.");
        }
    }
}
