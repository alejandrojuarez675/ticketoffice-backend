package com.ticketoffice.backend.domain.models;

public record Ticket(
        String id,
        Double value,
        String currency,
        String type,
        Boolean isFree,
        Integer stock
) {
}
