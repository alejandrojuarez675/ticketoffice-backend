package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import java.util.List;

public interface GetEventsByParamsUseCase {
    List<Event> getEventsByParams(String city, String query, Integer pageSize, Integer pageNumber);
}
