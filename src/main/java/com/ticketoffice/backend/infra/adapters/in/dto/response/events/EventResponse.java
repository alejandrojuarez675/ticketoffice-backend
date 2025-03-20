package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

public record EventResponse(
        String id,
        String name,
        String description,
        String date,
        String time,
        String place,
        String city,
        String country,
        String image,
        String category,
        String status,
        String type,
        String organizer,
        String organizerId,
        String organizerEmail,
        String organizerPhone,
        String organizerAddress,
        String organizerCity,
        String organizerCountry,
        String organizerImage,
        String organizerDescription,
        String organizerWebsite,
        String organizerSocialMedia
) {
}
