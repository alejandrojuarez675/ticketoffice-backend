package com.ticketoffice.backend.infra.adapters.in.dto.request;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.ImageDTO;

public record OrganizerCrudRequest(
        String name,
        String url,
        ImageDTO logo
) {
}
