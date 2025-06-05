package com.ticketoffice.backend.infra.adapters.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateSessionRequest(

        @Schema(description = "The ID of the event", example = "cbb46e0f-1014-41f9-bf63-ba1203197ce8")
        String eventId,

        @Schema(description = "The ID of the price of the ticket", example = "001b2f30-9a84-45e1-9345-518bea8a77c8")
        String priceId,

        @Schema(description = "The quantity of tickets to buy", example = "1", maximum = "5")
        Integer quantity
) {
}
