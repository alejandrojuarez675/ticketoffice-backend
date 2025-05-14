package com.ticketoffice.backend.infra.adapters.in.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record LocationDTO(

        @Schema(name = "The name of the location", example = "Movistar Arena")
        String name,

        @Schema(name = "The address of the location", example = "Dg. 61c #26-36")
        String address,

        @Schema(name = "The city of the location", example = "Bogotá")
        String city

) {
}
