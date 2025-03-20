package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import java.util.List;

public record TicketListResponse(
        List<TicketLightResponse> tickets
) {
}
