package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.List;

@FunctionalInterface
public interface GetAllMyEventsUseCase extends UseCase {
    List<Event> get() throws NotAuthenticatedException;
}
