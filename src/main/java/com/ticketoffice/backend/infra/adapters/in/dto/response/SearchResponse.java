package com.ticketoffice.backend.infra.adapters.in.dto.response;

import com.ticketoffice.backend.infra.adapters.in.dto.response.events.EventLightResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SearchResponse(

        @Schema(description = "The list of events to show in the search page")
        List<EventLightResponse> events,

        @Schema(description = "A boolean indicating if there are events in your city", example = "true")
        Boolean hasEventsInYourCity,

        @Schema(description = "The total number of pages", example = "10")
        Integer totalPages,

        @Schema(description = "The current page number", example = "1")
        Integer currentPage,

        @Schema(description = "The max number of events per page", example = "100")
        Integer pageSize
) {
}
