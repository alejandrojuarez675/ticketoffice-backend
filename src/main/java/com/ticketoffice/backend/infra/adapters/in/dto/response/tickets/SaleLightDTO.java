package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

public record SaleLightDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        String ticketType,
        Double price,
        Boolean validated
) {}
