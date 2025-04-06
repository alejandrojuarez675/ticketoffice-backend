package com.ticketoffice.backend.infra.adapters.in.dto.mocks;

import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventForVipResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventListResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.response.events.SocialMediasResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.EventListItemDTO;
import java.util.List;

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

    public static EventResponse eventResponse = new EventResponse(
            "1",
            "Event 1",
            "Description 1",
            "20215-12-12",
            "Time 1",
            "Location 1",
            "City 1",
            "Country 1",
            "Banner URL 1",
            "Category 1",
            "Status 1",
            "Type 1",
            "Organizer 1",
            "Organizer ID 1",
            "Organizer Email 1",
            "Organizer Phone 1",
            "Organizer Address 1",
            "Organizer City 1",
            "Organizer Country 1",
            "Organizer Image 1",
            "Organizer Description 1",
            "Organizer Website 1",
            "Organizer Social Media 1"
    );

    public static SocialMediasResponse socialMediasResponse = new SocialMediasResponse(
            "https://facebook.com/event1",
            "https://instagram.com/event1",
            "https://twitter.com/event1",
            "https://youtube.com/event1",
            "https://tiktok.com/event1",
            "https://website.com/event1"
    );

    public static EventForVipResponse eventForVipResponse = new EventForVipResponse(
            "1",
            "Event 1",
            "20215-12-12",
            "Time 1",
            "Location 1",
            "City 1",
            "Banner URL 1",
            "Category 1",
            "Organizer 1",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            socialMediasResponse
    );

    public static EventListItemDTO eventListItemDTO = new EventListItemDTO(
            "1",
            "Event 1",
            "20215-12-12",
            "Location 1",
            "Status 1",
            "Type 1"
    );

    public static EventListResponse eventListResponse = new EventListResponse(List.of(eventListItemDTO));
}
