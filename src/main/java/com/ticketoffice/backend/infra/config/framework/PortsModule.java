package com.ticketoffice.backend.infra.config.framework;

import com.google.inject.AbstractModule;

public class PortsModule extends AbstractModule {

    @Override
    protected void configure() {
        boolean isLocal = System.getProperty("environment", "local").equals("local");
        if (isLocal) {
            var mockedPortsModule = new MockedPortsModule();
            install(mockedPortsModule);
        } else {
            var prodPortsModule = new ProdPortsModule();
            install(prodPortsModule);
        }
    }
}
