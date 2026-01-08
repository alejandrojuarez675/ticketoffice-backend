package com.ticketoffice.backend.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;

public record UserToken(
        String id,
        String username,
        String email,
        String tokenHash,
        long expiresAt,
        boolean used
) {

    @JsonIgnore
    public boolean isValidToken() {
        boolean isExpired = Instant.now().getEpochSecond() > this.expiresAt();
        return !this.used() && !isExpired;
    }
}
