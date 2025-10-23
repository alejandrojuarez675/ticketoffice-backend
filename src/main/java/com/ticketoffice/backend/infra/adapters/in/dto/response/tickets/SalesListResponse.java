package com.ticketoffice.backend.infra.adapters.in.dto.response.tickets;

import java.util.List;

public record SalesListResponse(
        List<SaleLightDTO> sales
) {
}
