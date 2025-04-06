package com.ticketoffice.backend.infra.adapters.in.dto.response.checkout;

public record AvailableTicketResponse(
        String id,
        String eventId,
        String eventName,
        Double ticketPrice,
        String ticketName,
        Integer availableQuantity
) {
}
