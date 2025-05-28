package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;

public interface DeleteMyEventUseCase {
    void deleteMyEvent(String id) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
