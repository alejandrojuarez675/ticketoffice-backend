package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

import java.time.LocalDateTime;

public record EventLightResponse(
        String id,
        String name,
        LocalDateTime date,
        String location,
        String bannerUrl,
        Double price,
        String currency,
        String status
) {
}
