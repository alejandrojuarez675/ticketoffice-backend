package com.ticketoffice.backend.application.usecases.tickets;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.domain.usecases.events.GetEventUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetOnHoldTicketsStockUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetAvailableTicketStockIdUseCaseImpl implements GetAvailableTicketStockIdUseCase {

    private final GetEventUseCase getEventUseCase;
    private final GetOnHoldTicketsStockUseCase getOnHoldTicketsStockUseCase;

    public GetAvailableTicketStockIdUseCaseImpl(
            GetEventUseCase getEventUseCase,
            GetOnHoldTicketsStockUseCase getOnHoldTicketsStockUseCase
    ) {
        this.getEventUseCase = getEventUseCase;
        this.getOnHoldTicketsStockUseCase = getOnHoldTicketsStockUseCase;
    }

    @Override
    public Integer apply(String eventId, String ticketId) throws ResourceDoesntExistException {
        Event event = getEventUseCase.apply(eventId)
                .orElseThrow(() -> new ResourceDoesntExistException("Event not found"));

        Integer initialStock = event.prices().stream()
                .filter(price -> price.id().equals(ticketId))
                .findAny()
                .map(TicketPrice::stock)
                .orElseThrow(() -> new ResourceDoesntExistException("Ticket not found"));

        Integer onHoldStock = getOnHoldTicketsStockUseCase.apply(eventId, ticketId);

        return initialStock - onHoldStock; // TODO add sales
    }
}
