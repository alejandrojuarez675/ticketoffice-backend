package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.domain.usecases.password.ResetPasswordWithTokenUseCase;
import com.ticketoffice.backend.domain.usecases.password.UpdatePasswordUseCase;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class ResetPasswordWithTokenUseCaseImpl implements ResetPasswordWithTokenUseCase {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final MailSenderPort mailSenderPort;

    @Inject
    public ResetPasswordWithTokenUseCaseImpl(
            PasswordResetTokenRepository passwordResetTokenRepository,
            UpdatePasswordUseCase updatePasswordUseCase,
            MailSenderPort mailSenderPort
    ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public Boolean apply(String token, String newPassword) {
        return passwordResetTokenRepository.findByHashToken(token)
                .filter(PasswordResetToken::isValidToken)
                .flatMap(x -> {
                    Optional<User> user = updatePasswordUseCase.apply(x.username(), newPassword);
                    markPasswordTokenAsUsed(x);
                    return user;
                })
                .map(this::sendNotificationToUser)
                .isPresent();
    }

    private void markPasswordTokenAsUsed(PasswordResetToken x) {
        PasswordResetToken passToken = new PasswordResetToken(x.id(), x.username(), x.email(), x.tokenHash(), x.expiresAt(), true);
        passwordResetTokenRepository.save(passToken);
    }

    private User sendNotificationToUser(User user) {
        MailMessage mail = new MailMessage(
                MailTemplates.PASSWORD_UPDATED,
                user.getEmail(),
                Map.of(
                        "username", user.getUsername()
                )
        );
        mailSenderPort.sendEmail(mail);
        return user;
    }
}
