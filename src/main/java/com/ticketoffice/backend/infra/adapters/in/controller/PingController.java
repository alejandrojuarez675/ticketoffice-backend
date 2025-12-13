package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.Header;

public class PingController implements CustomController {

    @Override
    public void registeredRoutes(Javalin app) {
        app.get("/ping", ctx -> {
            // Set CORS headers
            ctx.header(Header.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            ctx.header(Header.ACCESS_CONTROL_ALLOW_METHODS, "GET, OPTIONS");
            ctx.header(Header.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization, X-Requested-With");
            
            // Handle preflight requests
            if (ctx.method().equals("OPTIONS")) {
                ctx.status(200);
                return;
            }
            
            ctx.contentType(ContentType.TEXT_PLAIN);
            ctx.result("pong");
        });
    }
}
