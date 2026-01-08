package com.ticketoffice.backend.domain.usecases.auth;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface ConfirmUserAccountUseCase extends UseCase, Function<String, Optional<User>> {
}
