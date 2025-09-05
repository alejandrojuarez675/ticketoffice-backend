package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.MarkAsConfirmingCheckoutSessionUseCase;

public class MarkAsConfirmingCheckoutSessionUseCaseImpl implements MarkAsConfirmingCheckoutSessionUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    public MarkAsConfirmingCheckoutSessionUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public CheckoutSession apply(String sessionId) throws ErrorOnPersistDataException {
        return checkoutSessionCache.updateStatus(sessionId, CheckoutSession.Status.CONFIRMING)
                .orElseThrow(() -> new ErrorOnPersistDataException("Checkout session cannot be updated"));
    }
}
