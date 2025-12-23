package com.ticketoffice.backend.domain.models;

public record Currency(
        String code,
        String name,
        String symbol
) {
}
