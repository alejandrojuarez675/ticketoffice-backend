package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import com.ticketoffice.backend.domain.utils.TriFunction;
import java.util.List;

@FunctionalInterface
public interface GetEventsByParamsUseCase extends UseCase, TriFunction<EventSearchParameters, Integer, Integer, List<Event>> {
}
