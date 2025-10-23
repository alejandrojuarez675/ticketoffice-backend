package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record OrganizerDTO(
        String id,
        String name,
        String url,
        ImageDTO logo
) {
}
