package com.ticketoffice.backend.application.usecases.tickets;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.usecases.sales.CountSalesByEventIdAndTicketIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetAvailableTicketStockIdUseCase;
import com.ticketoffice.backend.domain.usecases.tickets.GetOnHoldTicketsStockUseCase;

public class GetAvailableTicketStockIdUseCaseImpl implements GetAvailableTicketStockIdUseCase {

    private final GetOnHoldTicketsStockUseCase getOnHoldTicketsStockUseCase;
    private final CountSalesByEventIdAndTicketIdUseCase countSalesByEventIdAndTicketIdUseCase;

    public GetAvailableTicketStockIdUseCaseImpl(
            GetOnHoldTicketsStockUseCase getOnHoldTicketsStockUseCase,
            CountSalesByEventIdAndTicketIdUseCase countSalesByEventIdAndTicketIdUseCase
    ) {
        this.getOnHoldTicketsStockUseCase = getOnHoldTicketsStockUseCase;
        this.countSalesByEventIdAndTicketIdUseCase = countSalesByEventIdAndTicketIdUseCase;
    }

    @Override
    public Integer apply(Event event, String ticketId) throws ResourceDoesntExistException {
        Integer initialStock = event.tickets().stream()
                .filter(price -> price.id().equals(ticketId))
                .findAny()
                .map(Ticket::stock)
                .orElseThrow(() -> new ResourceDoesntExistException("Ticket not found"));

        Integer onHoldStock = getOnHoldTicketsStockUseCase.apply(event.id(), ticketId);
        Integer soldTickets = countSalesByEventIdAndTicketIdUseCase.apply(event.id(), ticketId);

        return initialStock - onHoldStock - soldTickets;
    }
}
