package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;

public class EventMocks {

    public final static EventLightResponse eventLightDTO = new EventLightResponse(
            "1",
            "Event 1",
            "20215-12-12",
            "Location 1",
            "City 1",
            "Category 1",
            "Banner URL 1",
            0.0
    );

}
