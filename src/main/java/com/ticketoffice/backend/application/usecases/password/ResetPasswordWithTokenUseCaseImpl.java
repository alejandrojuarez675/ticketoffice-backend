package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.domain.usecases.password.ResetPasswordWithTokenUseCase;
import com.ticketoffice.backend.domain.usecases.password.UpdatePasswordUseCase;
import jakarta.inject.Inject;
import java.time.Instant;

public class ResetPasswordWithTokenUseCaseImpl implements ResetPasswordWithTokenUseCase {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UpdatePasswordUseCase updatePasswordUseCase;

    @Inject
    public ResetPasswordWithTokenUseCaseImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UpdatePasswordUseCase updatePasswordUseCase
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.updatePasswordUseCase = updatePasswordUseCase;
    }

    @Override
    public Boolean apply(String token, String newPassword) {
        return passwordResetTokenRepository.findByHashToken(token)
                .filter(this::isValidToken)
                .map(x -> updatePasswordUseCase.apply(x.username(), newPassword))
                .isPresent();
    }

    private boolean isValidToken(PasswordResetToken x) {
        boolean isExpired = Instant.now().getEpochSecond() > x.expiresAt();
        return !x.used() && !isExpired;
    }
}
