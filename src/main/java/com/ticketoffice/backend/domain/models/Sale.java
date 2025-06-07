package com.ticketoffice.backend.domain.models;

import java.util.List;

public record Sale(
    String id,
    String eventId,
    String ticketId,
    Integer quantity,
    List<Buyer> buyer,
    String mainEmail
) {
    public Sale getCopyWithUpdatedId(String id) {
        return new Sale(
                id,
                this.eventId,
                this.ticketId,
                this.quantity,
                this.buyer,
                this.mainEmail
        );
    }
}
