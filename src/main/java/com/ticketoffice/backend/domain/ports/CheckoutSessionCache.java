package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import java.util.Optional;

public interface CheckoutSessionCache {

    Integer countKeysMatches(String pattern);

    Optional<CheckoutSession> getById(String sessionId);

    Optional<CheckoutSession> createCheckoutSession(CheckoutSession checkoutSession);

    void deleteById(String sessionId);

    Optional<CheckoutSession> updateStatus(String sessionId, CheckoutSession.Status status);
}
