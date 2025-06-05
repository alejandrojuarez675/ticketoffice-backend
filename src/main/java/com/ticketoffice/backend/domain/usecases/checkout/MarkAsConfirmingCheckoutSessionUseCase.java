package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.usecases.UseCase;

@FunctionalInterface
public interface MarkAsConfirmingCheckoutSessionUseCase extends UseCase {
    CheckoutSession apply(String sessionId) throws ErrorOnPersistDataException;
}
