package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import java.util.Optional;

public interface GetCheckoutSessionUseCase {
    Optional<CheckoutSession> getCheckoutSession(String sessionId);
}
