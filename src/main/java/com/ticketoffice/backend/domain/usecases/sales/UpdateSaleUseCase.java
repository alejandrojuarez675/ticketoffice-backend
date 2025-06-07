package com.ticketoffice.backend.domain.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.BiFunction;

@FunctionalInterface
public interface UpdateSaleUseCase extends UseCase, BiFunction<String, Sale, Optional<Sale>> {
}
