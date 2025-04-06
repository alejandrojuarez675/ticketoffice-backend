package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.checkout.CongratsResponse;

public class SalesMocks {

    public final static CongratsResponse congratsResponse = new CongratsResponse(
            "eventId",
            "eventName",
            "eventDate",
            "eventLocation",
            "eventCity",
            10.0,
            "ticketName",
            "2134213-fqwe52314-cwqe23454-dwe22334"
    );
}
