package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record EventCrudRequest(
        String name,
        String description,
        String date,
        String location,
        String city,
        String category,
        String bannerUrl) {
}
