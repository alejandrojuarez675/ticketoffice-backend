package com.ticketoffice.backend.domain.usecases.sales;

import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiFunction;

@FunctionalInterface
public interface CountSalesByEventIdAndTicketIdUseCase extends UseCase, BiFunction<String, String, Integer> {
}
