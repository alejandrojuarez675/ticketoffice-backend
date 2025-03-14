package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record EventLightDTO(
        String id,
        String name,
        String date,
        String location,
        String city,
        String category,
        String bannerUrl
) {
}
