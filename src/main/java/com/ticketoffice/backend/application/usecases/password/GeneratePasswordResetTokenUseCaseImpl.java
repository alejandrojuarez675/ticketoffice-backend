package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.domain.usecases.password.GeneratePasswordResetTokenUseCase;
import jakarta.inject.Inject;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

public class GeneratePasswordResetTokenUseCaseImpl implements GeneratePasswordResetTokenUseCase {

    public static final long EXPIRES_IN = 3600L * 5L; // 5 hours
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Inject
    public GeneratePasswordResetTokenUseCaseImpl(
            PasswordResetTokenRepository passwordResetTokenRepository
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordResetToken apply(User user) {
        PasswordResetToken passwordResetToken = generatePasswordResetToken(user);
        return passwordResetTokenRepository.save(passwordResetToken)
                .orElseThrow(() -> new RuntimeException("Can't generate new token to reset password"));
    }

    private PasswordResetToken generatePasswordResetToken(User user) {
        String token = generateToken();
        int count = 1;
        while (passwordResetTokenRepository.findByHashToken(token).isPresent()) {
            count ++;
            if (count == 5) {
                throw new RuntimeException("Can't generate new token to reset password, for repetition");
            }

            token = generateToken();
        }

        long expiresAt = LocalDateTime.now().plusSeconds(EXPIRES_IN).toInstant(ZoneOffset.UTC).getEpochSecond();

        return new PasswordResetToken(
                UUID.randomUUID().toString(),
                user.getUsername(),
                user.getEmail(),
                token,
                expiresAt,
                false
        );
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32]; // 256 bits
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
