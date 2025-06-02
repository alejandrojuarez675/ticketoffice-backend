package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetCheckoutSessionUseCase extends UseCase, Function<String, Optional<CheckoutSession>> {
}
