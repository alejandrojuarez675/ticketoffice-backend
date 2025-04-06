package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record BuyTicketsRequest(
        String eventId,
        String ticketId,
        Integer quantity,
        String buyerName,
        String buyerLastname,
        String buyerPhone,
        String buyerIdentification,
        String buyerEmail
) {
}
