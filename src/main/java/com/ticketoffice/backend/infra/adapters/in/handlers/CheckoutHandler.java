package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.application.usecases.checkout.CreateCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class CheckoutHandler {

    private final CreateCheckoutSessionUseCase createCheckoutSessionUseCase;

    public CheckoutHandler(CreateCheckoutSessionUseCase createCheckoutSessionUseCase) {
        this.createCheckoutSessionUseCase = createCheckoutSessionUseCase;
    }

    public SessionCreatedResponse createSession(CreateSessionRequest body) throws BadRequestException {

        CheckoutSession checkoutSession;
        try {
            checkoutSession = createCheckoutSessionUseCase.createCheckoutSession(
                    body.eventId(),
                    body.priceId(),
                    body.quantity());
        } catch (ResourceDoesntExistException | ProblemWithTicketStock e) {
            throw new BadRequestException(e.getMessage());
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }

        return new SessionCreatedResponse(
                checkoutSession.getId(),
                CreateCheckoutSessionUseCaseImpl.EXPIRATION_TIME_IN_SECONDS
        );
    }
}
