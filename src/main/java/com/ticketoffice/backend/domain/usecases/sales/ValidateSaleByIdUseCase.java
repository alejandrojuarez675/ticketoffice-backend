package com.ticketoffice.backend.domain.usecases.sales;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.exception.TicketValidatedPreviouslyException;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface ValidateSaleByIdUseCase extends UseCase {
    void accept(String id) throws ResourceDoesntExistException, TicketValidatedPreviouslyException, ErrorOnPersistDataException;
}
