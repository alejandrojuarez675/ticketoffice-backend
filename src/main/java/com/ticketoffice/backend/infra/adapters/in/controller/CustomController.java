package com.ticketoffice.backend.infra.adapters.in.controller;

import io.javalin.Javalin;

public interface CustomController {
    void registeredRoutes(Javalin app);
}
