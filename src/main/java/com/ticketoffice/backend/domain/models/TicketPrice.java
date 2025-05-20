package com.ticketoffice.backend.domain.models;

public record TicketPrice(
        String id,
        Double value,
        String currency,
        String type,
        Boolean isFree
) {
}
