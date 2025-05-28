package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import java.util.Optional;

public interface GetMyEventUseCase {

    Optional<Event> getMyEventById(String id) throws NotAuthenticatedException;
}
