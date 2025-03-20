package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

public record TicketResponse(
        String id,
        String eventId,
        String userId,
        String status,
        String type,
        Double price
) {
}
