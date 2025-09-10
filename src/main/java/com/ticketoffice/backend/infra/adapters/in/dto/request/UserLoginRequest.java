package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record UserLoginRequest (

    //@Schema(description = "The username of the user", example = "user123")
    String username,

    //@Schema(description = "The password of the user", example = "password123")
    String password
) {
}
