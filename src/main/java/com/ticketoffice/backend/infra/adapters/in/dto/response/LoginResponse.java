package com.ticketoffice.backend.infra.adapters.in.dto.response;

public record LoginResponse (
        String token,
        long expiresIn
        ) {
}
