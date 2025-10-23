package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record UserSignupRequest(
    String username,
    String password,
    String email
) {
}
