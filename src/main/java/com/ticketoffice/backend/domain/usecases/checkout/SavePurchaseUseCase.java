package com.ticketoffice.backend.domain.usecases.checkout;

import com.ticketoffice.backend.domain.models.Purchase;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

public interface SavePurchaseUseCase extends UseCase, Function<Purchase, Optional<Purchase>> {
}
