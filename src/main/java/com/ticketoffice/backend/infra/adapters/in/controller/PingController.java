package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.ticketoffice.backend.infra.adapters.in.dto.HealthCheckResponse;
import java.time.Instant;

public class PingController implements CustomController {

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/ping", this::ping);
        app.get("/health", this::healthCheck);
    }

    @Operation(
        summary = "Ping endpoint",
        description = "A simple ping endpoint that returns 'pong' to verify the API is running",
        tags = {"Health"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            )
        }
    )
    private void ping(Context ctx) {
        ctx.result("pong");
    }

    @Operation(
        summary = "Health check endpoint",
        description = "Returns the health status of the application with a timestamp",
        tags = {"Health"},
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Application is healthy",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = HealthCheckResponse.class))
            )
        }
    )
    private void healthCheck(Context ctx) {
        HealthCheckResponse response = new HealthCheckResponse(
            "healthy", 
            Instant.now().toString()
        );
        ctx.json(response);
    }
}
