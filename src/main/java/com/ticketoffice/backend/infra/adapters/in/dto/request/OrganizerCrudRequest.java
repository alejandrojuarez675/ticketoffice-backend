package com.ticketoffice.backend.infra.adapters.in.dto.request;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;

public record OrganizerCrudRequest(
        //@Schema(description = "The name of the organizer", example = "Movistar Arena")
        String name,

        //@Schema(description = "The description of the organizer", example = "https://movistararena.co/")
        String url,

        //@Schema(description = "The logo of the organizer")
        ImageDTO logo
) {
}
