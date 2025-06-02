package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.application.usecases.tickets.GetAvailableTicketStockIdUseCaseImpl;
import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class CreateCheckoutSessionUseCaseImpl implements CreateCheckoutSessionUseCase {

    public static final Long EXPIRATION_TIME_IN_SECONDS = 600L; // 10 minutes

    private final CheckoutSessionCache checkoutSessionCache;
    private final GetAvailableTicketStockIdUseCaseImpl getAvailableTicketStockIdUseCase;

    public CreateCheckoutSessionUseCaseImpl(
            CheckoutSessionCache checkoutSessionCache,
            GetAvailableTicketStockIdUseCaseImpl getAvailableTicketStockIdUseCase
    ) {
        this.checkoutSessionCache = checkoutSessionCache;
        this.getAvailableTicketStockIdUseCase = getAvailableTicketStockIdUseCase;
    }

    @Override
    public CheckoutSession createCheckoutSession(
            String eventId,
            String priceId,
            Integer quantity
    ) throws ResourceDoesntExistException, ProblemWithTicketStock, ErrorOnPersistDataException {

        Integer availableTicketStock = getAvailableTicketStockIdUseCase.getAvailableTicketStock(eventId, priceId);
        if (availableTicketStock < quantity) {
            throw new ProblemWithTicketStock("Not enough tickets available");
        }

        CheckoutSession checkoutSession = new CheckoutSession(
                CheckoutSessionIdUtils.createCheckoutSessionId(eventId, priceId, quantity),
                eventId,
                priceId,
                quantity,
                LocalDateTime.now().plusSeconds(EXPIRATION_TIME_IN_SECONDS)
        );

        return checkoutSessionCache.createCheckoutSession(checkoutSession)
                .orElseThrow(() -> new ErrorOnPersistDataException("Checkout session could not be created"));
    }
}
