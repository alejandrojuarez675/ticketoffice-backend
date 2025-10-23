package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record LocationDTO(
        String name,
        String address,
        String city,
        String country

) {
}
