package com.ticketoffice.backend.domain.usecases.password;

import com.ticketoffice.backend.domain.models.User;
import java.util.Optional;
import java.util.function.BiFunction;

@FunctionalInterface
public interface UpdatePasswordUseCase extends BiFunction<String, String, Optional<User>> {
}
