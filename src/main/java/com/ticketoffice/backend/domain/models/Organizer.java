package com.ticketoffice.backend.domain.models;

public record Organizer(
        String id,
        String name,
        String url,
        Image logo
) {
}
