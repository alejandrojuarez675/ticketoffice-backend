package com.ticketoffice.backend.infra.config.framework;

import com.google.inject.Injector;
import com.ticketoffice.backend.infra.adapters.in.controller.PingController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.EventsController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.OrganizerController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.SalesController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.TestEmailController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.UserController;
import com.ticketoffice.backend.infra.adapters.in.controller.auth.AuthenticationController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.CheckoutController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.EventDetailPageController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.SearchPageController;
import com.ticketoffice.backend.infra.adapters.in.controller.form.ContactUsController;
import com.ticketoffice.backend.infra.adapters.in.controller.form.RegionalizationFormController;
import com.ticketoffice.backend.infra.adapters.in.exception.handler.ApiExceptionHandler;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPlugin;
import io.javalin.plugin.bundled.CorsPluginConfig;

public class AppFactory {

    public static Javalin create(Injector injector) {
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.registerPlugin(
                    new CorsPlugin(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost))
            );
        });

        registerRoutes(injector, app);
        registerExceptionHandler(app);

        return app;
    }

    private static void registerRoutes(Injector injector, Javalin app) {
        // Public
        injector.getInstance(AuthenticationController.class).registeredRoutes(app);
        injector.getInstance(CheckoutController.class).registeredRoutes(app);
        injector.getInstance(SearchPageController.class).registeredRoutes(app);
        injector.getInstance(EventDetailPageController.class).registeredRoutes(app);
        injector.getInstance(PingController.class).registeredRoutes(app);

        // Authenticated
        injector.getInstance(EventsController.class).registeredRoutes(app);
        injector.getInstance(OrganizerController.class).registeredRoutes(app);
        injector.getInstance(SalesController.class).registeredRoutes(app);
        injector.getInstance(UserController.class).registeredRoutes(app);
        injector.getInstance(TestEmailController.class).registeredRoutes(app);
        injector.getInstance(RegionalizationFormController.class).registeredRoutes(app);
        injector.getInstance(ContactUsController.class).registeredRoutes(app);
    }

    private static void registerExceptionHandler(Javalin app) {
        // Register exception handler
        app.exception(Exception.class, new ApiExceptionHandler());
    }
}
