package com.ticketoffice.backend.infra.adapters.in.dto.response.checkout;

public record BuyTicketResponse(
        Boolean success,
        String congratsToken
) {
}
