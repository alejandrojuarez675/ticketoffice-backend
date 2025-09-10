package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import java.util.List;

public record SalesListResponse(
        //@Schema(description = "List of sales")
        List<SaleLightDTO> sales
) {
}
