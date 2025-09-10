package com.ticketoffice.backend.infra.adapters.in.dto.shared;

public record OrganizerDTO(

        //@Schema(description = "The ID of the organizer", example = "001b2f30-9a84-45e1-9345-518bea8a77c8")
        String id,

        //@Schema(description = "The name of the organizer", example = "Movistar Arena")
        String name,

        //@Schema(description = "The description of the organizer", example = "https://movistararena.co/")
        String url,

        //@Schema(description = "The logo of the organizer")
        ImageDTO logo
) {
}
