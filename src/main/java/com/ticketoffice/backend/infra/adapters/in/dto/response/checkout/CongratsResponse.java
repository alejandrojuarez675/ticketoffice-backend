package com.ticketoffice.backend.infra.adapters.in.dto.response.checkout;

public record CongratsResponse(
        String eventId,
        String eventName,
        String eventDate,
        String eventLocation,
        String eventCity,
        Double ticketPrice,
        String ticketName,
        String saleId
) {
}
