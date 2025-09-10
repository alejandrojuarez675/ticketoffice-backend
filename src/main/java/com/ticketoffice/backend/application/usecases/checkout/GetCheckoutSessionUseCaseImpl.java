package com.ticketoffice.backend.application.usecases.checkout;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import java.util.Optional;

public class GetCheckoutSessionUseCaseImpl implements GetCheckoutSessionUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    @Inject
    public GetCheckoutSessionUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public Optional<CheckoutSession> apply(String sessionId) {
        return checkoutSessionCache.getById(sessionId);
    }
}
