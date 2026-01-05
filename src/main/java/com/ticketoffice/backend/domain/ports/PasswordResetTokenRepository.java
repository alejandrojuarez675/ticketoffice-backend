package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import java.util.Optional;

public interface PasswordResetTokenRepository {
    Optional<PasswordResetToken> save(PasswordResetToken model);
    Optional<PasswordResetToken> findByHashToken(String hashToken);
}
