package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.events.CountEventsByParamsUseCase;
import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SearchResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SearchPageHandler {

    private final GetEventsByParamsUseCase getEventsByParamsUseCase;
    private final CountEventsByParamsUseCase countEventsByParamsUseCase;

    public SearchPageHandler(
            GetEventsByParamsUseCase getEventsByParamsUseCase,
            CountEventsByParamsUseCase countEventsByParamsUseCase
    ) {
        this.getEventsByParamsUseCase = getEventsByParamsUseCase;
        this.countEventsByParamsUseCase = countEventsByParamsUseCase;
    }

    public SearchResponse getEventsByParams(String country, String city, String query, Integer pageSize, Integer pageNumber) {
        EventSearchParameters eventSearchParameters = new EventSearchParameters(country, city, query);
        List<Event> events = getEventsByParamsUseCase.getEventsByParams(eventSearchParameters, pageSize, pageNumber);

        return new SearchResponse(
                events.stream().map(EventLightResponseMapper::getFromEvent).toList(),
                !events.isEmpty(),
                countEventsByParamsUseCase.countEventsByParams(eventSearchParameters)/pageSize + 1,
                pageNumber,
                pageSize
        );
    }
}
