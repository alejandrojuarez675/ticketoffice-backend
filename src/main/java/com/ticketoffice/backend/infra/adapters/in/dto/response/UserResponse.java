package com.ticketoffice.backend.infra.adapters.in.dto.response;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.OrganizerDTO;
import java.util.List;

public record UserResponse(
        String id,
        String username,
        String email,
        List<String> role,
        OrganizerDTO organizer
) {
}
