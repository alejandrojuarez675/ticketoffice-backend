package com.ticketoffice.backend.application.usecases.checkout;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.checkout.GetCheckoutSessionUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetCheckoutSessionUseCaseImpl implements GetCheckoutSessionUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    public GetCheckoutSessionUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public Optional<CheckoutSession> getCheckoutSession(String sessionId) {
        return checkoutSessionCache.getById(sessionId);
    }
}
