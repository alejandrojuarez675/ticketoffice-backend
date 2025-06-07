package com.ticketoffice.backend.domain.models;

import java.util.List;

public record Sale(
    String id,
    String eventId,
    String ticketId,
    Integer quantity,
    Double price,
    List<Buyer> buyer,
    String mainEmail,
    Boolean validated
) {
    public Sale getCopyWithUpdatedId(String id) {
        return new Sale(
                id,
                this.eventId,
                this.ticketId,
                this.quantity,
                this.price,
                this.buyer,
                this.mainEmail,
                this.validated
        );
    }

    public Sale getCopyWithUpdatedValidated(boolean value) {
        return new Sale(
                this.id,
                this.eventId,
                this.ticketId,
                this.quantity,
                this.price,
                this.buyer,
                this.mainEmail,
                value
        );
    }
}
