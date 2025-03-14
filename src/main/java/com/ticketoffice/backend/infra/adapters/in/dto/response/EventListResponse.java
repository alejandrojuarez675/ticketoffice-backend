package com.ticketoffice.backend.infra.adapters.in.dto.response;

import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventListItemDTO;
import java.util.List;

public record EventListResponse(
        List<EventListItemDTO> events
) {
}
