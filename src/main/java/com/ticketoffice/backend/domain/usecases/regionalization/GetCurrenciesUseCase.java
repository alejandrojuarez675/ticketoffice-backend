package com.ticketoffice.backend.domain.usecases.regionalization;

import com.ticketoffice.backend.domain.models.Currency;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface GetCurrenciesUseCase extends Function<String, List<Currency>> {
}
