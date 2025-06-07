package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.application.usecases.tickets.GetAvailableTicketStockIdUseCaseImpl;
import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class CreateCheckoutSessionUseCaseImpl implements CreateCheckoutSessionUseCase {

    public static final Long EXPIRATION_TIME_IN_SECONDS = 600L; // 10 minutes

    private final CheckoutSessionCache checkoutSessionCache;
    private final GetEventUseCase getEventUseCase;
    private final GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase;

    public CreateCheckoutSessionUseCaseImpl(
            CheckoutSessionCache checkoutSessionCache,
            GetEventUseCase getEventUseCase,
            GetAvailableTicketStockIdUseCaseImpl getAvailableTicketStockIdUseCase
    ) {
        this.checkoutSessionCache = checkoutSessionCache;
        this.getEventUseCase = getEventUseCase;
        this.getAvailableTicketStockIdUseCase = getAvailableTicketStockIdUseCase;
    }

    @Override
    public CheckoutSession apply(
            String eventId,
            String priceId,
            Integer quantity
    ) throws ResourceDoesntExistException, ProblemWithTicketStock, ErrorOnPersistDataException {

        Event event = getEventUseCase.apply(eventId)
                .orElseThrow(() -> new ResourceDoesntExistException("Event not found"));

        Integer availableTicketStock = getAvailableTicketStockIdUseCase.apply(event, priceId);

        if (availableTicketStock < quantity) {
            throw new ProblemWithTicketStock("Not enough sales available");
        }

        CheckoutSession checkoutSession = new CheckoutSession(
                CheckoutSessionIdUtils.createCheckoutSessionId(eventId, priceId, quantity),
                eventId,
                priceId,
                quantity,
                CheckoutSession.Status.CREATED,
                LocalDateTime.now().plusSeconds(EXPIRATION_TIME_IN_SECONDS)
        );

        return checkoutSessionCache.createCheckoutSession(checkoutSession)
                .orElseThrow(() -> new ErrorOnPersistDataException("Checkout session could not be created"));
    }
}
