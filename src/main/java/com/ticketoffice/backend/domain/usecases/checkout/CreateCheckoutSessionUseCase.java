package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ProblemWithTicketStock;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.models.CheckoutSession;

public interface CreateCheckoutSessionUseCase {
    CheckoutSession createCheckoutSession(String eventId, String priceId, Integer quantity)
            throws ResourceDoesntExistException, ProblemWithTicketStock, ErrorOnPersistDataException;
}
