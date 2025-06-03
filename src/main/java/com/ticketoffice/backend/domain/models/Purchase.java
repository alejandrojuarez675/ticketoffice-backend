package com.ticketoffice.backend.domain.models;

import java.util.List;

public record Purchase(
    String id,
    String eventId,
    String priceId,
    Integer quantity,
    List<Buyer> buyer,
    String mainEmail
) {
    public Purchase getCopyWithUpdatedId(String id) {
        return new Purchase(
                id,
                this.eventId,
                this.priceId,
                this.quantity,
                this.buyer,
                this.mainEmail
        );
    }
}
