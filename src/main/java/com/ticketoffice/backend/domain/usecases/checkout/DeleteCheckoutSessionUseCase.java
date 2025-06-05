package com.ticketoffice.backend.domain.usecases.checkout;

import java.util.function.Consumer;

@FunctionalInterface
public interface DeleteCheckoutSessionUseCase extends Consumer<String> {
}
