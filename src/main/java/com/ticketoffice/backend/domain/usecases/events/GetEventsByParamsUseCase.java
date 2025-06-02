package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import java.util.List;
import org.apache.commons.lang3.function.TriFunction;

@FunctionalInterface
public interface GetEventsByParamsUseCase extends UseCase, TriFunction<EventSearchParameters, Integer, Integer, List<Event>> {
}
