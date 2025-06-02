package com.ticketoffice.backend.domain.usecases.events;

import com.ticketoffice.backend.domain.usecases.UseCase;
import com.ticketoffice.backend.domain.utils.EventSearchParameters;
import java.util.function.Function;

@FunctionalInterface
public interface CountEventsByParamsUseCase extends UseCase, Function<EventSearchParameters, Integer> {
}
