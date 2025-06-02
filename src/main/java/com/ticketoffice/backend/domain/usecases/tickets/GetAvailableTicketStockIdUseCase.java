package com.ticketoffice.backend.domain.usecases.tickets;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface GetAvailableTicketStockIdUseCase extends UseCase {
    Integer apply(String eventId, String ticketId) throws ResourceDoesntExistException;
}
