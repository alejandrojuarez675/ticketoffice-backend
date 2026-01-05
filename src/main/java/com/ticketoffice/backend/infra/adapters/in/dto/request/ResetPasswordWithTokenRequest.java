package com.ticketoffice.backend.infra.adapters.in.dto.request;

public record ResetPasswordWithTokenRequest(
        String token,
        String newPassword
) {
}
