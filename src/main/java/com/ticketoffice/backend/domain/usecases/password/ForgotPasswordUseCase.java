package com.ticketoffice.backend.domain.usecases.password;

import com.ticketoffice.backend.domain.models.User;
import java.util.function.Consumer;

@FunctionalInterface
public interface ForgotPasswordUseCase extends Consumer<User> {
}
