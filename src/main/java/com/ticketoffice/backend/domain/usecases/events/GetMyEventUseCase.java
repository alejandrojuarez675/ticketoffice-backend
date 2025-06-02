package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;

@FunctionalInterface
public interface GetMyEventUseCase extends UseCase {

    Optional<Event> apply(String id) throws NotAuthenticatedException;
}
