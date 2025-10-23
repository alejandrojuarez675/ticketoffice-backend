package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record TicketDTO(
        String id,
        Double value,
        String currency,
        String type,
        Boolean isFree,
        Integer stock
) {
}
