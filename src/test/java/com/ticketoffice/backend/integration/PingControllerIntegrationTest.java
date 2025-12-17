package com.ticketoffice.backend.integration;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.ticketoffice.backend.infra.adapters.in.controller.PingController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PingControllerIntegrationTest {
    
    private Javalin app;
    private OkHttpClient client;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        // Start the application with the same CORS configuration as Main
        app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.registerPlugin(new CorsPlugin(cors ->
                cors.addRule(CorsPluginConfig.CorsRule::anyHost)
            ));
        });

        // Register routes
        new PingController().registeredRoutes(app);

        // Start the server on a random port
        app.start(0);
        baseUrl = String.format("http://localhost:%d", app.port());

        // Configure HTTP client
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    @AfterEach
    void tearDown() {
        if (app != null) {
            app.stop();
        }
    }

    @Nested
    class PingGetEndpointTests {
        @Test
        void getPing_shouldReturn200() throws IOException {
            Request request = new Request.Builder()
                    .url(baseUrl + "/ping")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                assertEquals(200, response.code(), "Should return 200 OK status code");
            }
        }

        @Test
        void getPing_shouldReturnPong() throws IOException {
            Request request = new Request.Builder()
                    .url(baseUrl + "/ping")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                assertNotNull(response.body());
                assertEquals("pong", response.body().string(), "Should return 'pong' in response body");
            }
        }
    }

    @Nested
    class CorsTests {

        @Test
        void shouldAllowCrossOriginRequests() throws IOException {
            Request request = new Request.Builder()
                    .url(baseUrl + "/ping")
                    .header("Origin", "https://example.com")
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                assertEquals(200, response.code(), "Should allow CORS requests");
            }
        }
    }

    @Nested
    class OptionsPreflightTests {
        @Test
        void optionsRequest_shouldReturn200() throws IOException {
            Request request = new Request.Builder()
                    .url(baseUrl + "/ping")
                    .header("Origin", "https://example.com")
                    .header("Access-Control-Request-Method", "GET")
                    .method("OPTIONS", null)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                assertEquals(200, response.code(), "Should return 200 for OPTIONS preflight request");
            }
        }

        @Test
        void optionsRequest_shouldIncludeCorsHeaders() throws IOException {
            Request request = new Request.Builder()
                    .url(baseUrl + "/ping")
                    .header("Origin", "https://example.com")
                    .header("Access-Control-Request-Method", "GET")
                    .method("OPTIONS", null)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                Headers headers = response.headers();
                assertAll("CORS headers in OPTIONS response",
                    () -> assertEquals("*", headers.get("Access-Control-Allow-Origin"),
                            "Should include Access-Control-Allow-Origin header"),
                    () -> assertNotNull(headers.get("Access-Control-Allow-Methods"),
                            "Should include Access-Control-Allow-Methods header")
                );
            }
        }
    }
}
