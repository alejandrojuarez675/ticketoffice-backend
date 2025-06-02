package com.ticketoffice.backend.infra.adapters.in.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record PriceDTO(

        @Schema(description = "The ID of the price", example = "001b2f30-9a84-45e1-9345-518bea8a77c8")
        String id,

        @Schema(description = "The value of the price", example = "100.0")
        Double value,

        @Schema(description = "The currency of the price", example = "$")
        String currency,

        @Schema(description = "The type of the price", example = "General")
        String type,

        @Schema(description = "The description of the price", example = "false")
        Boolean isFree,

        @Schema(description = "The stock of the price", example = "100")
        Integer stock
) {
}
