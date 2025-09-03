package com.ticketoffice.backend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ticketoffice.backend.infra.adapters.in.controller.PingController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.EventsController;
import com.ticketoffice.backend.infra.config.AppModule;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
//            config.registerPlugin(new OpenApiPlugin(...)); // si quieres OpenAPI
        });

        injector.getInstance(PingController.class).registeredRoutes(app);
        injector.getInstance(EventsController.class).registeredRoutes(app);

        app.start(8080);
    }
}
