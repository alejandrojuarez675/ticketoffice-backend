package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.TicketRepository;
import com.ticketoffice.backend.domain.usecases.checkout.DeleteCheckoutSessionUseCase;
import com.ticketoffice.backend.domain.usecases.checkout.RegisterPurchaseUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RegisterPurchaseUseCaseImpl implements RegisterPurchaseUseCase {

    private final TicketRepository ticketRepository;
    private final GetEventUseCase getEventUseCase;
    private final SendConfirmationEmailToBuyerUseCase sendConfirmationEmailToBuyerUseCase;
    private final SendTicketEmailToBuyerUseCase sendTicketEmailToBuyerUseCase;
    private final DeleteCheckoutSessionUseCase deleteCheckoutSessionUseCase;
    private final GetAvailableTicketStockIdUseCase getAvailableTicketStockIdUseCase;

    public RegisterPurchaseUseCaseImpl(
            TicketRepository ticketRepository,
            GetEventUseCase getEventUseCase,
            SendConfirmationEmailToBuyerUseCase sendConfirmationEmailToBuyerUseCase,
            SendTicketEmailToBuyerUseCase sendTicketEmailToBuyerUseCase,
    ) {
        this.ticketRepository = ticketRepository;
        this.getEventUseCase = getEventUseCase;
        this.sendConfirmationEmailToBuyerUseCase = sendConfirmationEmailToBuyerUseCase;
        this.sendTicketEmailToBuyerUseCase = sendTicketEmailToBuyerUseCase;
        this.deleteCheckoutSessionUseCase = deleteCheckoutSessionUseCase;
        this.getAvailableTicketStockIdUseCase = getAvailableTicketStockIdUseCase;
    }

    @Override
    public void accept(String sessionId, Ticket ticket) {
        Event event = getEventUseCase.apply(ticket.eventId())
                .orElseThrow(() -> new RuntimeException("Event cannot be found"));

        try {
            Integer availableQuantity = getAvailableTicketStockIdUseCase.apply(event, ticket.priceId());
            if (availableQuantity < 0) {
                throw new RuntimeException("Not enough tickets available");
            }
        } catch (ResourceDoesntExistException e) {
            throw new RuntimeException(e);
        }

        ticket.buyer()
                .stream()
                .map(buyer -> new Ticket(
                        UUID.randomUUID().toString(),
                        ticket.eventId(),
                        ticket.priceId(),
                        ticket.quantity(),
                        List.of(buyer),
                        buyer.email()
                ))
                .map(ticketRepository::save)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(t -> sendTicketEmailToBuyerUseCase.accept(t, event));

        sendConfirmationEmailToBuyerUseCase.accept(ticket, event);
        deleteCheckoutSessionUseCase.accept(sessionId);
    }
}
