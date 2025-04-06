package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

public record EventLightResponse(
        String id,
        String name,
        String date,
        String location,
        String city,
        String category,
        String bannerUrl,
        Double price
) {
}
