package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

public record SocialMediasResponse(
        String facebook,
        String instagram,
        String twitter,
        String youtube,
        String tiktok,
        String website
) {
}
