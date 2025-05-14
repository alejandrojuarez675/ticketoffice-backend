package com.ticketoffice.backend.infra.adapters.in.dto.shared;

import io.swagger.v3.oas.annotations.media.Schema;

public record ImageDTO(

        @Schema(description = "The URL of the image", example = "https://movistararena.co/wp-content/uploads/2025/02/trueno-2025-4.jpg")
        String url,

        @Schema(description = "The alt text of the image", example = "Concierto de Trueno")
        String alt

) {
}
