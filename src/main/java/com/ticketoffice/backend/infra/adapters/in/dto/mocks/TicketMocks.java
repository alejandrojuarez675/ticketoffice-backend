package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.AvailableTicketListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.AvailableTicketResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.BuyTicketResponse;
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

    public static BuyTicketResponse buyTicketResponse = new BuyTicketResponse(
            true,
            "1234-4352fed-dfgsdsf4-fwsadfdg-23423f"
    );

    public static final AvailableTicketListResponse availableTicketListResponse = new AvailableTicketListResponse(
            List.of(
                    new AvailableTicketResponse(
                            "1",
                            "eventId",
                            "eventName",
                            12.0,
                            "ticketName",
                            15
                    )
            ),
            "information"
    );
}
