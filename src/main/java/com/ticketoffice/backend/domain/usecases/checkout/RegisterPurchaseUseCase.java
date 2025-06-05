package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface RegisterPurchaseUseCase extends UseCase, BiConsumer<String, Ticket> {
}
