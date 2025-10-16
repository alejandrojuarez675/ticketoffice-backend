package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetAuthenticatedUserUseCase extends UseCase, Function<Context, Optional<User>> {
}
