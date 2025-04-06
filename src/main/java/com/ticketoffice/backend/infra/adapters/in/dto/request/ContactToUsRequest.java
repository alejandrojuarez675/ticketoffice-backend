package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record ContactToUsRequest(
        String name,
        String email,
        String message,
        String originUrl
) {
}
