package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

public record EventForVipResponse(
        String id,
        String name,
        String date,
        String time,
        String place,
        String city,
        String image,
        String category,
        String organizer,
        String information,
        SocialMediasResponse socialMedias,
        Boolean hasTicketStock
) {
}
