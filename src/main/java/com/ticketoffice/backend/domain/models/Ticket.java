package com.ticketoffice.backend.domain.models;

import java.util.List;

public record Ticket(
    String id,
    String eventId,
    String priceId,
    Integer quantity,
    List<Buyer> buyer,
    String mainEmail
) {
    public Ticket getCopyWithUpdatedId(String id) {
        return new Ticket(
                id,
                this.eventId,
                this.priceId,
                this.quantity,
                this.buyer,
                this.mainEmail
        );
    }
}
