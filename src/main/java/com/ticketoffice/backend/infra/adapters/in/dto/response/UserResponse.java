package com.ticketoffice.backend.infra.adapters.in.dto.response;

import java.util.List;

public record UserResponse(
        String id,
        String username,
        String email,
        List<String> role
) {
}
