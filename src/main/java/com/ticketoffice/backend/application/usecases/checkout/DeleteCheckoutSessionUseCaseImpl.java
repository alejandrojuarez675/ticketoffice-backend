package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.DeleteCheckoutSessionUseCase;

public class DeleteCheckoutSessionUseCaseImpl implements DeleteCheckoutSessionUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    public DeleteCheckoutSessionUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public void accept(String sessionId) {
        checkoutSessionCache.deleteById(sessionId);
    }
}
