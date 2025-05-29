package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.events.GetEventsByParamsUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.EventLightResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.SearchResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SearchPageHandler {

    private final GetEventsByParamsUseCase getEventsByParamsUseCase;

    public SearchPageHandler(GetEventsByParamsUseCase getEventsByParamsUseCase) {
        this.getEventsByParamsUseCase = getEventsByParamsUseCase;
    }

    public SearchResponse getEventsByParams(String city, String query, Integer pageSize, Integer pageNumber) {
        List<Event> events = getEventsByParamsUseCase.getEventsByParams(city, query, pageSize, pageNumber);

        return new SearchResponse(
                events.stream().map(EventLightResponseMapper::getFromEvent).toList(),
                !events.isEmpty()
        );
    }
}
