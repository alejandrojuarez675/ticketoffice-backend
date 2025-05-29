package com.ticketoffice.backend.domain.models;

public record Location(
        String id,
        String name,
        String address,
        String city,
        String country
) {
}
