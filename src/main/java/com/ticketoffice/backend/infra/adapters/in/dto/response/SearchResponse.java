package com.ticketoffice.backend.infra.adapters.in.dto.response;

import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import java.util.List;

public record SearchResponse(
        List<EventLightResponse> events,
        Boolean hasEventsInYourCity,
        Integer totalPages,
        Integer currentPage,
        Integer pageSize
) {
}
