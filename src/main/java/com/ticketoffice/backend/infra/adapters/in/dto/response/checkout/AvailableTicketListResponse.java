package com.ticketoffice.backend.infra.adapters.in.dto.response.checkout;

import java.util.List;

public record AvailableTicketListResponse(
        List<AvailableTicketResponse> tickets,
        String information
) {
}
