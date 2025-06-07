package com.ticketoffice.backend.domain.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetSaleByIdUseCase extends UseCase, Function<String, Optional<Sale>> {
}
