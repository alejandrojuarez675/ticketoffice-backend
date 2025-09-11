package com.ticketoffice.backend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ticketoffice.backend.infra.adapters.in.controller.PingController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.EventsController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.OrganizerController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.SalesController;
import com.ticketoffice.backend.infra.adapters.in.controller.admin.UserController;
import com.ticketoffice.backend.infra.adapters.in.controller.auth.AuthenticationController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.CheckoutController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.EventDetailPageController;
import com.ticketoffice.backend.infra.adapters.in.controller.checkout.SearchPageController;
import com.ticketoffice.backend.infra.config.AppModule;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
//            config.registerPlugin(new OpenApiPlugin(pluginConfig ->
//                    pluginConfig.withDefinitionConfiguration((version, definition) ->
//                            definition.withInfo(openApiInfo ->
//                                    openApiInfo.setTitle("Ticket backoffice")
//                            )
//                    )
//            ));
//            config.registerPlugin(new SwaggerPlugin());
//            config.registerPlugin(new ReDocPlugin());
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
        
        app.start(8080);
    }
}
