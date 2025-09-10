package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record CreateSessionRequest(

        //@Schema(description = "The ID of the event", example = "cd85b222-2adf-414d-aa26-6a0fb7c87beb")
        String eventId,

        //@Schema(description = "The ID of the price of the ticket", example = "001b2f30-9a84-45e1-9345-518bea8a77c8")
        String priceId,

        //@Schema(description = "The quantity of sales to buy", example = "1", maximum = "5")
        Integer quantity
) {
}
