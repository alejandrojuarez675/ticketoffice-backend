package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetUserByIdUseCase extends UseCase, Function<String, Optional<User>> {
}
