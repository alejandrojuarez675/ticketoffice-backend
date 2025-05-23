package com.ticketoffice.backend.infra.adapters.in.dto.response.events;

import java.util.List;

public record EventListResponse(
        List<EventLightResponse> events
) {
}
