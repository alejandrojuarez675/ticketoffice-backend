package com.ticketoffice.backend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ticketoffice.backend.infra.adapters.in.controller.PingController;
import com.ticketoffice.backend.infra.adapters.in.controller.TestEmailController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.EventsController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.OrganizerController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.SalesController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.UserController;
import com.ticketoffice.backend.infra.adapters.in.controller.auth.AuthenticationController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.CheckoutController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.EventDetailPageController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.SearchPageController;
import com.ticketoffice.backend.infra.adapters.in.controller.form.RegionalizationFormController;
import com.ticketoffice.backend.infra.adapters.in.exception.handler.ApiExceptionHandler;
import com.ticketoffice.backend.infra.config.AppModule;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {

            Injector injector = Guice.createInjector(new AppModule());
            Javalin app = Javalin.create(config -> {
                config.http.defaultContentType = "application/json";
                config.registerPlugin(new CorsPlugin(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost)));
            });

            // Public
            injector.getInstance(AuthenticationController.class).registeredRoutes(app);
            injector.getInstance(CheckoutController.class).registeredRoutes(app);
            injector.getInstance(SearchPageController.class).registeredRoutes(app);
            injector.getInstance(EventDetailPageController.class).registeredRoutes(app);

            // Authenticated
            injector.getInstance(PingController.class).registeredRoutes(app);
            injector.getInstance(EventsController.class).registeredRoutes(app);
            injector.getInstance(OrganizerController.class).registeredRoutes(app);
            injector.getInstance(SalesController.class).registeredRoutes(app);
            injector.getInstance(UserController.class).registeredRoutes(app);
            injector.getInstance(TestEmailController.class).registeredRoutes(app);
            injector.getInstance(RegionalizationFormController.class).registeredRoutes(app);

            // Register exception handler
            app.exception(Exception.class, new ApiExceptionHandler());

            int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
            System.out.println("Starting server on port: " + port);
            app.start(port);
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
