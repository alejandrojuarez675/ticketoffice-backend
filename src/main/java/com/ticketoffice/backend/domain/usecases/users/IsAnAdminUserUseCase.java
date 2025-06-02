package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface IsAnAdminUserUseCase extends BooleanSupplier, UseCase {
}
