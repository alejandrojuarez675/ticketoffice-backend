package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import java.util.List;

public interface GetEventsByParamsUseCase {
    List<Event> getEventsByParams(EventSearchParameters eventSearchParameters, Integer pageSize, Integer pageNumber);
}
