package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface GetAuthenticatedUserUseCase extends UseCase, Supplier<Optional<User>> {
}
