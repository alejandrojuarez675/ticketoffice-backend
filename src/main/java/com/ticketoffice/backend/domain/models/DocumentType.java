package com.ticketoffice.backend.domain.models;

public record DocumentType(
        String code,
        String name,
        String description,
        String format,
        String regex
) {
}
