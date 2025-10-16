package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.usecases.UseCase;
import io.javalin.http.Context;
import java.util.function.Function;

@FunctionalInterface
public interface IsAnAdminUserUseCase extends Function<Context, Boolean>, UseCase {
}
