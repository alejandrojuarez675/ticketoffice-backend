package com.ticketoffice.backend.domain.usecases.tickets;

public interface GetOnHoldTicketsStockUseCase {
    Integer getOnHoldTicketsStock(String eventId, String ticketId);
}
