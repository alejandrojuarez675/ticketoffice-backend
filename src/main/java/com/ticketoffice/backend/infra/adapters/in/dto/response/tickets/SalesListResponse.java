package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record SalesListResponse(
        @Schema(description = "List of sales")
        List<SaleLightDTO> sales
) {
}
