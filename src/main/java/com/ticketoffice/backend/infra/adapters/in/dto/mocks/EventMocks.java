package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.EventForVipResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventLightDTO;

public class EventMocks {

    public final static EventLightDTO eventLightDTO = new EventLightDTO(
        "1",
        "Event 1",
        "20215-12-12",
        "Location 1",
        "City 1",
        "Category 1",
        "Banner URL 1"
    );

    public static EventForVipResponse eventForVipResponse = new EventForVipResponse();
}
