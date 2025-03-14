package com.ticketoffice.backend.infra.adapters.in.dto.response;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventLightDTO;
import java.util.List;

public record SearchResponse(
        List<EventLightDTO> events,
        Boolean hasEventsInYourCity
) {
}
