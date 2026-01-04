package com.ticketoffice.backend.domain.usecases.password;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import java.util.function.Function;

@FunctionalInterface
public interface GeneratePasswordResetTokenUseCase extends Function<User, PasswordResetToken> {
}
