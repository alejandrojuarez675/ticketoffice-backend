package com.ticketoffice.backend.infra.adapters.in.dto.response;

public record ErrorResponse(
        String message,
        String code
) {
}
