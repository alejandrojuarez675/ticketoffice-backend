package com.ticketoffice.backend.infra.adapters.in.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserSignupRequest(

    @Schema(description = "The username of the user", example = "user123")
    String username,

    @Schema(description = "The password of the user", example = "password123")
    String password,

    @Schema(description = "The email of the user", example = "example@gmail.com")
    String email
) {
}
