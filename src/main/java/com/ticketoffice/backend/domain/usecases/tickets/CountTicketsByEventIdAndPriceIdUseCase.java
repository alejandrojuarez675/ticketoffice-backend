package com.ticketoffice.backend.domain.usecases.tickets;

import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiFunction;

@FunctionalInterface
public interface CountTicketsByEventIdAndPriceIdUseCase extends UseCase, BiFunction<String, String, Integer> {
}
