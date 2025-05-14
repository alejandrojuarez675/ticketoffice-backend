package com.ticketoffice.backend.domain.models;

import java.time.LocalDateTime;
import java.util.List;

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
}
