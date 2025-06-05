package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.application.usecases.checkout.CreateCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.RegisterPurchaseUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.TicketMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class CheckoutHandler {

    private final CreateCheckoutSessionUseCase createCheckoutSessionUseCase;
    private final GetCheckoutSessionUseCase getCheckoutSessionUseCase;
    private final RegisterPurchaseUseCase registerPurchaseUseCase;

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

    public void buyTickets(String sessionId, BuyTicketsRequest request) throws BadRequestException {
        CheckoutSession checkoutSession = getCheckoutSessionUseCase.apply(sessionId)
                .orElseThrow(() -> new BadRequestException("Session not found"));

        if (checkoutSession.getQuantity() != request.buyer().size()) {
            throw new BadRequestException("Invalid number of buyers");
        }

        Ticket ticket = TicketMapper.getTicketFromBuyTickets(checkoutSession, request);

        registerPurchaseUseCase.accept(sessionId, ticket);
    }
}
