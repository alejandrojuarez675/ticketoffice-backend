package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record UserLoginRequest (
    String username,
    String password
) {
}
