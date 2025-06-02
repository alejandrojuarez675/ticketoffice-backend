package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface DeleteMyEventUseCase extends UseCase {
    void accept(String id) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
