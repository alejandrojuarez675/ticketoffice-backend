package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record BuyTicketsRequest(
        String eventId,
        Integer quantity
) {
}
