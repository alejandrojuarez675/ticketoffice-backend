package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.UseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.CongratsCheckout;
import java.util.function.BiFunction;

@FunctionalInterface
public interface RegisterPurchaseUseCase extends UseCase, BiFunction<String, Sale,CongratsCheckout> {
}
