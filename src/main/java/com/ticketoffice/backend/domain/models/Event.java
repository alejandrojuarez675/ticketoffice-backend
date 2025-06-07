package com.ticketoffice.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ticketoffice.backend.domain.enums.EventStatus;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record Event(
        String id,
        String title,
        LocalDateTime date,
        Location location,
        Image image,
        List<Ticket> tickets,
        String description,
        List<String> additionalInfo,
        String organizerId,
        EventStatus status
){

    @JsonIgnore
    public Double getTheCheapestPrice() {
        return Optional.ofNullable(tickets)
                .filter(prices -> !prices.isEmpty())
                .map(
                        prices -> prices.stream()
                            .map(price -> price.isFree() ? 0.0 : price.value())
                            .min(Comparator.comparingDouble(Double::doubleValue))
                            .orElse(0.0) // Default value if no sales are found
                )
                .orElse(null);
    }

    @JsonIgnore
    public Event getCopyWithUpdatedStatus(EventStatus status) {
        return new Event(
                id,
                title,
                date,
                location,
                image,
                tickets,
                description,
                additionalInfo,
                organizerId,
                status
        );
    }

    @JsonIgnore
    public Event getCopyWithUpdatedId(String id) {
        return new Event(
                id,
                title,
                date,
                location,
                image,
                tickets,
                description,
                additionalInfo,
                organizerId,
                status
        );
    }
}
