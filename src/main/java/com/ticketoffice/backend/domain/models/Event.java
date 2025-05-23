package com.ticketoffice.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        List<TicketPrice> prices,
        String description,
        List<String> additionalInfo,
        Organizer organizer
){

    @JsonIgnore
    public Double getTheCheapestPrice() {
        return Optional.ofNullable(prices)
                .filter(prices -> !prices.isEmpty())
                .map(
                        prices -> prices.stream()
                            .map(price -> price.isFree() ? 0.0 : price.value())
                            .min(Comparator.comparingDouble(Double::doubleValue))
                            .orElse(0.0) // Default value if no prices are found
                )
                .orElse(null);
    }
}
