package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface CreateCheckoutSessionUseCase extends UseCase {
    CheckoutSession apply(String eventId, String priceId, Integer quantity)
            throws ResourceDoesntExistException, ProblemWithTicketStock, ErrorOnPersistDataException;
}
