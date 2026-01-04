package com.ticketoffice.backend.domain.models;

public record PasswordResetToken(
        String id,
        String username,
        String email,
        String tokenHash,
        long expiresAt,
        boolean used
) {
}
