package com.ticketoffice.backend.infra.adapters.in.dto.response;

public record SessionCreatedResponse(
    String sessionId,
    Long expiredIn
) {
}
