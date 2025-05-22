package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventListItemDTO;
import java.util.List;

public record EventListResponse(
        List<EventDetailPageResponse> events
) {
}
