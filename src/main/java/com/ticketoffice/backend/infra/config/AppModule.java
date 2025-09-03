package com.ticketoffice.backend.infra.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ticketoffice.backend.application.usecases.users.GetAuthenticatedUserUseCaseImpl;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventCrudHandler;
import com.ticketoffice.backend.infra.adapters.out.db.repository.UserInMemoryRepository;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventCrudHandler.class).to(EventCrudHandler.class).in(Singleton.class);
        bind(UserRoleValidator.class).to(UserRoleValidator.class).in(Singleton.class);
        bind(GetAuthenticatedUserUseCase.class).to(GetAuthenticatedUserUseCaseImpl.class).in(Singleton.class);
        bind(UserRepository.class).to(UserInMemoryRepository.class);
        // Binding para EntityManager / Datasource si usas JPA/Hibernate
    }
}
