package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record CreateSessionRequest(
        String eventId,
        String priceId,
        Integer quantity
) {
}
