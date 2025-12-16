package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class PingControllerTest {
    
    private Javalin app;
    private HttpClient client;
    
    @BeforeEach
    void setUp() {
        app = Javalin.create();
        new PingController().registeredRoutes(app);
        app.start(0);
        client = HttpClient.newHttpClient();
    }
    
    @AfterEach
    void tearDown() {
        app.stop();
    }

    @Test
    void ping_shouldReturnOkStatus() throws Exception {
        // Given
        int port = app.port();
        String url = String.format("http://localhost:%d/ping", port);
        System.out.println("Testing URL: " + url);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        // When
        System.out.println("Sending request...");
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Then
        assertEquals(200, response.statusCode(), 
                "Expected status code 200 but got " + response.statusCode());
    }
    
    @Test
    void ping_shouldReturnPongInResponseBody() throws Exception {
        // Given
        int port = app.port();
        String url = String.format("http://localhost:%d/ping", port);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        // When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Then
        assertEquals("pong", response.body(),
                "Expected response body 'pong' but got '" + response.body() + "'");
    }
    
    @Test
    void ping_shouldReturnJsonContentType() throws Exception {
        // Given
        int port = app.port();
        String url = String.format("http://localhost:%d/ping", port);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        
        // When
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Then
        String contentType = response.headers().firstValue("Content-Type")
                .orElse("<missing>");
                
        assertTrue(contentType.startsWith("application/json"),
                String.format("Content-Type should start with 'application/json' but was '%s'", 
                        contentType));
    }
}
