package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record TicketListResponse(
        @Schema(description = "List of tickets")
        List<TicketLightDTO> tickets
) {
}
