package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;

@FunctionalInterface
public interface DeleteMyEventUseCase extends UseCase {
    void accept(Context context, String id) throws NotAuthenticatedException, ErrorOnPersistDataException;
}
