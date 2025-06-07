package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import io.swagger.v3.oas.annotations.media.Schema;

public record SaleLightDTO(
        @Schema(description = "The ID of the ticket", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "The buyer's first name", example = "John")
        String firstName,

        @Schema(description = "The buyer's last name", example = "Doe")
        String lastName,

        @Schema(description = "The buyer's email", example = "john.doe@example.com")
        String email,

        @Schema(description = "The type of the ticket price", example = "VIP")
        String ticketType,

        @Schema(description = "The price of the ticket", example = "150.50")
        Double price,

        @Schema(description = "The validation status of the ticket", example = "true")
        Boolean validated
) {}
