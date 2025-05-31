package com.ticketoffice.backend.domain.usecases.tickets;

import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;

public interface GetAvailableTicketStockIdUseCase {
    Integer getAvailableTicketStock(String eventId, String ticketId) throws ResourceDoesntExistException;
}
