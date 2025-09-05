package com.ticketoffice.backend.infra.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ticketoffice.backend.application.usecases.checkout.CreateCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.application.usecases.checkout.DeleteCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.application.usecases.checkout.GetCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.application.usecases.checkout.MarkAsConfirmingCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.application.usecases.checkout.RegisterPurchaseUseCaseImpl;
import com.ticketoffice.backend.application.usecases.emails.SendConfirmationEmailToBuyerUseCaseImpl;
import com.ticketoffice.backend.application.usecases.emails.SendTicketEmailToBuyerUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.CountEventsByParamsUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.CreateEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.DeleteMyEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.GetAllMyEventsUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.GetEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.GetEventsByParamsUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.GetMyEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.GetSimilarEventsToAnEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.events.UpdateMyEventUseCaseImpl;
import com.ticketoffice.backend.application.usecases.organizer.CreateOrganizerUseCaseImpl;
import com.ticketoffice.backend.application.usecases.organizer.GetOrganizerByUserIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.organizer.GetOrganizerByUserUseCaseImpl;
import com.ticketoffice.backend.application.usecases.sales.CountSalesByEventIdAndTicketIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.sales.GetAllSalesByEventIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.sales.GetSaleByIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.sales.UpdateSaleUseCaseImpl;
import com.ticketoffice.backend.application.usecases.sales.ValidateSaleByIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.tickets.GetAvailableTicketStockIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.tickets.GetOnHoldTicketsStockUseCaseImpl;
import com.ticketoffice.backend.application.usecases.users.GetAllUsersUserCaseImpl;
import com.ticketoffice.backend.application.usecases.users.GetAuthenticatedUserUseCaseImpl;
import com.ticketoffice.backend.application.usecases.users.GetUserByIdUseCaseImpl;
import com.ticketoffice.backend.application.usecases.users.IsAnAdminUserUseCaseImpl;
import com.ticketoffice.backend.application.usecases.users.UpdateOrganizerDataOnUserUseCaseImpl;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.ports.EmailService;
import com.ticketoffice.backend.domain.ports.EventRepository;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.DeleteCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.MarkAsConfirmingCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.RegisterPurchaseUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.events.CountEventsByParamsUseCase;
import com.ticketoffice.backend.domain.usecases.events.CreateEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.DeleteMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetAllMyEventsUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetSimilarEventsToAnEventUseCase;
import com.ticketoffice.backend.domain.usecases.events.UpdateMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.CreateOrganizerUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserUseCase;
import com.ticketoffice.backend.domain.usecases.sales.CountSalesByEventIdAndTicketIdUseCase;
import com.ticketoffice.backend.domain.usecases.sales.GetAllSalesByEventIdUseCase;
import com.ticketoffice.backend.domain.usecases.sales.GetSaleByIdUseCase;
import com.ticketoffice.backend.domain.usecases.sales.UpdateSaleUseCase;
import com.ticketoffice.backend.domain.usecases.sales.ValidateSaleByIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetOnHoldTicketsStockUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAllUsersUserCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetUserByIdUseCase;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.UpdateOrganizerDataOnUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.controller.UserRoleValidator;
import com.ticketoffice.backend.infra.adapters.in.handlers.CheckoutHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventCrudHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.EventDetailPageHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.OrganizerCrudHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.SalesHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.SearchPageHandler;
import com.ticketoffice.backend.infra.adapters.in.handlers.UserHandler;
import com.ticketoffice.backend.infra.adapters.out.cache.CheckoutSessionInMemoryCache;
import com.ticketoffice.backend.infra.adapters.out.db.repository.EventInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.SaleInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.db.repository.UserInMemoryRepository;
import com.ticketoffice.backend.infra.adapters.out.emails.EmailServiceImpl;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        // handlers
        bind(EventCrudHandler.class).to(EventCrudHandler.class);
        bind(CheckoutHandler.class).to(CheckoutHandler.class).in(Singleton.class);
        bind(EventDetailPageHandler.class).to(EventDetailPageHandler.class);
        bind(OrganizerCrudHandler.class).to(OrganizerCrudHandler.class);
        bind(SalesHandler.class).to(SalesHandler.class);
        bind(SearchPageHandler.class).to(SearchPageHandler.class);
        bind(UserHandler.class).to(UserHandler.class);

        // validators
        bind(UserRoleValidator.class).to(UserRoleValidator.class);

        // usecases
        bind(GetAuthenticatedUserUseCase.class).to(GetAuthenticatedUserUseCaseImpl.class);
        bind(CreateCheckoutSessionUseCase.class).to(CreateCheckoutSessionUseCaseImpl.class);
        bind(DeleteCheckoutSessionUseCase.class).to(DeleteCheckoutSessionUseCaseImpl.class);
        bind(GetCheckoutSessionUseCase.class).to(GetCheckoutSessionUseCaseImpl.class);
        bind(MarkAsConfirmingCheckoutSessionUseCase.class).to(MarkAsConfirmingCheckoutSessionUseCaseImpl.class);
        bind(RegisterPurchaseUseCase.class).to(RegisterPurchaseUseCaseImpl.class);
        bind(SendConfirmationEmailToBuyerUseCase.class).to(SendConfirmationEmailToBuyerUseCaseImpl.class);
        bind(SendTicketEmailToBuyerUseCase.class).to(SendTicketEmailToBuyerUseCaseImpl.class);
        bind(CountEventsByParamsUseCase.class).to(CountEventsByParamsUseCaseImpl.class);
        bind(CreateEventUseCase.class).to(CreateEventUseCaseImpl.class);
        bind(DeleteMyEventUseCase.class).to(DeleteMyEventUseCaseImpl.class);
        bind(GetAllMyEventsUseCase.class).to(GetAllMyEventsUseCaseImpl.class);
        bind(GetEventsByParamsUseCase.class).to(GetEventsByParamsUseCaseImpl.class);
        bind(GetEventUseCase.class).to(GetEventUseCaseImpl.class);
        bind(GetMyEventUseCase.class).to(GetMyEventUseCaseImpl.class);
        bind(GetSimilarEventsToAnEventUseCase.class).to(GetSimilarEventsToAnEventUseCaseImpl.class);
        bind(UpdateMyEventUseCase.class).to(UpdateMyEventUseCaseImpl.class);
        bind(CreateOrganizerUseCase.class).to(CreateOrganizerUseCaseImpl.class);
        bind(GetOrganizerByUserIdUseCase.class).to(GetOrganizerByUserIdUseCaseImpl.class);
        bind(GetOrganizerByUserUseCase.class).to(GetOrganizerByUserUseCaseImpl.class);
        bind(CountSalesByEventIdAndTicketIdUseCase.class).to(CountSalesByEventIdAndTicketIdUseCaseImpl.class);
        bind(GetAllSalesByEventIdUseCase.class).to(GetAllSalesByEventIdUseCaseImpl.class);
        bind(GetSaleByIdUseCase.class).to(GetSaleByIdUseCaseImpl.class);
        bind(UpdateSaleUseCase.class).to(UpdateSaleUseCaseImpl.class);
        bind(ValidateSaleByIdUseCase.class).to(ValidateSaleByIdUseCaseImpl.class);
        bind(GetAvailableTicketStockIdUseCase.class).to(GetAvailableTicketStockIdUseCaseImpl.class);
        bind(GetOnHoldTicketsStockUseCase.class).to(GetOnHoldTicketsStockUseCaseImpl.class);
        bind(GetAllUsersUserCase.class).to(GetAllUsersUserCaseImpl.class);
        bind(GetUserByIdUseCase.class).to(GetUserByIdUseCaseImpl.class);
        bind(IsAnAdminUserUseCase.class).to(IsAnAdminUserUseCaseImpl.class);
        bind(UpdateOrganizerDataOnUserUseCase.class).to(UpdateOrganizerDataOnUserUseCaseImpl.class);

        // TODO review user usecases
        bind(GetAuthenticatedUserUseCase.class).to(GetAuthenticatedUserUseCaseImpl.class);

        // service
        bind(EmailService.class).to(EmailServiceImpl.class);

        // repositories
        bind(UserRepository.class).to(UserInMemoryRepository.class);
        bind(EventRepository.class).to(EventInMemoryRepository.class);
        bind(SaleRepository.class).to(SaleInMemoryRepository.class);
        bind(CheckoutSessionCache.class).to(CheckoutSessionInMemoryCache.class);
        // Binding para EntityManager / Datasource si usas JPA/Hibernate
    }
}
