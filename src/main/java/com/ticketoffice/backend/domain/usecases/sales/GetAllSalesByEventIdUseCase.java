package com.ticketoffice.backend.domain.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;

import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface GetAllSalesByEventIdUseCase extends UseCase, Function<String, List<Sale>> {
}
