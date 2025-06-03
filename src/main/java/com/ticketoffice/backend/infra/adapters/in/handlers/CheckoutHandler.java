package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.application.usecases.checkout.CreateCheckoutSessionUseCaseImpl;
import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Purchase;
import com.ticketoffice.backend.domain.usecases.checkout.CreateCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.SavePurchaseUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.PurchaseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.CreateSessionRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SessionCreatedResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class CheckoutHandler {

    private final CreateCheckoutSessionUseCase createCheckoutSessionUseCase;
    private final GetCheckoutSessionUseCase getCheckoutSessionUseCase;
    private final SavePurchaseUseCase savePurchaseUseCase;

    public CheckoutHandler(
            CreateCheckoutSessionUseCase createCheckoutSessionUseCase,
            GetCheckoutSessionUseCase getCheckoutSessionUseCase,
            SavePurchaseUseCase savePurchaseUseCase
    ) {
        this.createCheckoutSessionUseCase = createCheckoutSessionUseCase;
        this.getCheckoutSessionUseCase = getCheckoutSessionUseCase;
        this.savePurchaseUseCase = savePurchaseUseCase;
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

        Purchase purchase = PurchaseMapper.getPurchase(checkoutSession, request);
        savePurchaseUseCase.apply(purchase)
                .orElseThrow(() -> new RuntimeException("Problems to register the purchase"));
    }
}
