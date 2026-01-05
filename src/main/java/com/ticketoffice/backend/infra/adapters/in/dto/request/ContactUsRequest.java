package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record ContactUsRequest(
        String name,
        String email,
        String message,
        String subject
) {
}
