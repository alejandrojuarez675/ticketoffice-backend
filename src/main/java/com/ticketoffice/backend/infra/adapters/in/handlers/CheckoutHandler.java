package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.usecases.checkout.CreateCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.RegisterPurchaseUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.TicketMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.CongratsCheckout;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;

public class CheckoutHandler {

    private final CreateCheckoutSessionUseCase createCheckoutSessionUseCase;
    private final GetCheckoutSessionUseCase getCheckoutSessionUseCase;
    private final RegisterPurchaseUseCase registerPurchaseUseCase;

    @Inject
    public CheckoutHandler(
            CreateCheckoutSessionUseCase createCheckoutSessionUseCase,
            GetCheckoutSessionUseCase getCheckoutSessionUseCase,
            RegisterPurchaseUseCase registerPurchaseUseCase
    ) {
        this.createCheckoutSessionUseCase = createCheckoutSessionUseCase;
        this.getCheckoutSessionUseCase = getCheckoutSessionUseCase;
        this.registerPurchaseUseCase = registerPurchaseUseCase;
    }

    public SessionCreatedResponse createSession(CreateSessionRequest body) throws BadRequestException {

        CheckoutSession checkoutSession;
        try {
            checkoutSession = createCheckoutSessionUseCase.apply(
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

    public CongratsCheckout buyTickets(String sessionId, BuyTicketsRequest request) throws BadRequestException {
        CheckoutSession checkoutSession = getCheckoutSessionUseCase.apply(sessionId)
                .orElseThrow(() -> new BadRequestException("Session not found"));

        if (checkoutSession.getQuantity() != request.buyer().size()) {
            throw new BadRequestException("Invalid number of buyers");
        }

        if (!CheckoutSession.Status.CREATED.equals(checkoutSession.getStatus())) {
            throw new BadRequestException("Session is not ready");
        }

        Sale sale = TicketMapper.getTicketFromBuyTickets(checkoutSession, request);

        return registerPurchaseUseCase.apply(sessionId, sale);
    }
}
