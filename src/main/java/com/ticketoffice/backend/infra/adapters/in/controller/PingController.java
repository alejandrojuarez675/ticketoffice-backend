package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;
import java.util.Map;

public class PingController implements CustomController {

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/ping", ctx -> ctx.json("pong"));

        // Añadir health check endpoint
        app.get("/health", ctx -> {
            ctx.status(200).json(Map.of("status", "healthy"));
        });
    }
}
