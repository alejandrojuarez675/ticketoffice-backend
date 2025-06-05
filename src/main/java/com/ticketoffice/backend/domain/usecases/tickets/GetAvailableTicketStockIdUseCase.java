package com.ticketoffice.backend.domain.usecases.tickets;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface GetAvailableTicketStockIdUseCase extends UseCase {
    Integer apply(Event event, String ticketId) throws ResourceDoesntExistException;
}
