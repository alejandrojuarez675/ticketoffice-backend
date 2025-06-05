package com.ticketoffice.backend.domain.models;

public record Buyer(
        String id,
        String name,
        String lastName,
        String email,
        String phone,
        String nationality,
        String documentType,
        String document
) {
}
