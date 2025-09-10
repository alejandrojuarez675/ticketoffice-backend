package com.ticketoffice.backend.application.usecases.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.checkout.DeleteCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.RegisterPurchaseUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegisterPurchaseUseCaseImpl implements RegisterPurchaseUseCase {

    private final SaleRepository saleRepository;
    private final GetEventUseCase getEventUseCase;
    private final SendConfirmationEmailToBuyerUseCase sendConfirmationEmailToBuyerUseCase;
    private final SendTicketEmailToBuyerUseCase sendTicketEmailToBuyerUseCase;
    private final DeleteCheckoutSessionUseCase deleteCheckoutSessionUseCase;
    private final GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase;

    @Inject
    public RegisterPurchaseUseCaseImpl(
            SaleRepository saleRepository,
            GetEventUseCase getEventUseCase,
            SendConfirmationEmailToBuyerUseCase sendConfirmationEmailToBuyerUseCase,
            SendTicketEmailToBuyerUseCase sendTicketEmailToBuyerUseCase,
            DeleteCheckoutSessionUseCase deleteCheckoutSessionUseCase,
            GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase
    ) {
        this.saleRepository = saleRepository;
        this.getEventUseCase = getEventUseCase;
        this.sendConfirmationEmailToBuyerUseCase = sendConfirmationEmailToBuyerUseCase;
        this.sendTicketEmailToBuyerUseCase = sendTicketEmailToBuyerUseCase;
        this.deleteCheckoutSessionUseCase = deleteCheckoutSessionUseCase;
        this.getAvailableTicketStockIdUseCase = getAvailableTicketStockIdUseCase;
    }

    @Override
    public void accept(String sessionId, Sale sale) {
        Event event = getEventUseCase.apply(sale.eventId())
                .orElseThrow(() -> new RuntimeException("Event cannot be found"));

        try {
            Integer availableQuantity = getAvailableTicketStockIdUseCase.apply(event, sale.ticketId());
            if (availableQuantity < 0) {
                throw new RuntimeException("Not enough sales available");
            }
        } catch (ResourceDoesntExistException e) {
            throw new RuntimeException(e);
        }

        Double price = event.tickets()
                .stream()
                .filter(t -> t.id().equals(sale.ticketId()))
                .findFirst()
                .map(Ticket::value)
                .orElseThrow(() -> new RuntimeException("Ticket cannot be found"));

        sale.buyer()
                .stream()
                .map(buyer -> new Sale(
                        UUID.randomUUID().toString(),
                        sale.eventId(),
                        sale.ticketId(),
                        sale.quantity(),
                        price,
                        List.of(buyer),
                        buyer.email(),
                        Boolean.FALSE
                ))
                .map(saleRepository::save)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(t -> sendTicketEmailToBuyerUseCase.accept(t, event));

        sendConfirmationEmailToBuyerUseCase.accept(sale, event);
        deleteCheckoutSessionUseCase.accept(sessionId);
    }
}
