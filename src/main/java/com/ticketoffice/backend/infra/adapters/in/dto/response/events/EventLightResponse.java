package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record EventLightResponse(

        @Schema(description = "The id of the event", example = "dsdsf-1234-1234-1234")
        String id,

        @Schema(description = "The name of the event", example = "Concierto de Trueno")
        String name,

        @Schema(description = "The date of the event", example = "2022-01-01T20:00:00")
        LocalDateTime date,

        @Schema(description = "The location of the event formatted to show", example = "Movistar Arena")
        String location,

        @Schema(description = "The link to banner of the event", example = "https://movistararena.co/wp-content/uploads/2025/02/trueno-2025-4.jpg")
        String bannerUrl,

        @Schema(description = "The minimum price of the event", example = "100000")
        Double price,

        @Schema(description = "The currency of the price", example = "COP")
        String currency,

        @Schema(description = "The status of the event", example = "ACTIVE")
        String status
) {
}
