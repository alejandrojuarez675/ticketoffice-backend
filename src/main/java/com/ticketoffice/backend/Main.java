package com.ticketoffice.backend;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ticketoffice.backend.infra.config.framework.AppFactory;
import com.ticketoffice.backend.infra.config.framework.AppModule;
import com.ticketoffice.backend.infra.config.framework.PortsModule;
import com.ticketoffice.backend.infra.config.framework.UseCaseModule;
import io.javalin.Javalin;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {

            Injector injector = Guice.createInjector(
                    new UseCaseModule(),
                    new PortsModule(),
                    new AppModule()
            );

            Javalin app = AppFactory.create(injector);

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
