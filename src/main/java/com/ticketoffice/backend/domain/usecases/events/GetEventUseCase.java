package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetEventUseCase extends UseCase, Function<String, Optional<Event>> {
}
