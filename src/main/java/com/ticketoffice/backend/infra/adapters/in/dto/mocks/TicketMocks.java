package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketLightResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.TicketResponse;
import java.util.List;

public class TicketMocks {

    public static TicketResponse TicketResponse = new TicketResponse(
            "1",
            "1",
            "1",
            "1",
            "1",
            1.0
    );

    public static TicketLightResponse ticketLightResponse = new TicketLightResponse(
            "1",
            "1",
            "1",
            "1",
            "1",
            1.0
    );

    public static TicketListResponse ticketListResponse = new TicketListResponse(List.of(ticketLightResponse));
}
