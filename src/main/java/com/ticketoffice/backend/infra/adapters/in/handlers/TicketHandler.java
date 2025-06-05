package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAllTicketsByEventIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketLightDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketHandler {

    private final GetAllTicketsByEventIdUseCase getAllTicketsByEventIdUseCase;
    private final GetMyEventUseCase getMyEventUseCase;

    public TicketHandler(
            GetAllTicketsByEventIdUseCase getAllTicketsByEventIdUseCase,
            GetMyEventUseCase getMyEventUseCase
    ) {
        this.getAllTicketsByEventIdUseCase = getAllTicketsByEventIdUseCase;
        this.getMyEventUseCase = getMyEventUseCase;
    }

    public TicketListResponse getAllTicketsByEventId(String eventId) throws NotAuthenticatedException, NotFoundException {
        Event event = getMyEventUseCase.apply(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        List<Ticket> tickets = getAllTicketsByEventIdUseCase.apply(eventId);

        return new TicketListResponse(
            tickets.stream()
                .map(ticket -> {
                    var buyer = ticket.buyer().getFirst();

                    return event.prices().stream()
                            .filter(p -> p.id().equals(ticket.priceId()))
                            .findFirst()
                            .map(ticketPrice -> new TicketLightDTO(
                                    ticket.id(),
                                    buyer.name(),
                                    buyer.lastName(),
                                    buyer.email(),
                                    ticketPrice.type(),
                                    ticket.quantity() * ticketPrice.value()
                            ));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList())
        );
    }
}
