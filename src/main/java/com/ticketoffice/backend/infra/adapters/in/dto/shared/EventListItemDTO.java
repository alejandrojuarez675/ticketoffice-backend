package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record EventListItemDTO(
        String id,
        String name,
        String date,
        String location,
        String status,
        String type
) {
}
