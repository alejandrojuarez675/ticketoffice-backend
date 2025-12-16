package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;

public class PingController implements CustomController {

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/ping", ctx -> ctx.json("pong"));
    }
}
