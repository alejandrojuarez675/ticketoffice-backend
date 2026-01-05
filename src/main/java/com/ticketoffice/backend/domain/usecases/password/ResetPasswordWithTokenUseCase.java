package com.ticketoffice.backend.domain.usecases.password;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ResetPasswordWithTokenUseCase extends BiFunction<String, String, Boolean> {
}
