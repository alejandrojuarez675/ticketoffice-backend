package com.ticketoffice.backend.integration.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ticketoffice.backend.infra.config.framework.AppFactory;
import com.ticketoffice.backend.infra.config.framework.AppModule;
import com.ticketoffice.backend.infra.config.framework.MockedPortsModule;
import com.ticketoffice.backend.infra.config.framework.UseCaseModule;
import io.javalin.testtools.JavalinTest;
import io.javalin.testtools.TestCase;

public class MyJavalinTest {
    public static void test(TestCase testFunc) {
        Injector injector = Guice.createInjector(
                new UseCaseModule(),
                new MockedPortsModule(),
                new AppModule()
        );

        JavalinTest.test(
                AppFactory.create(injector),
                testFunc
        );
    }
}
