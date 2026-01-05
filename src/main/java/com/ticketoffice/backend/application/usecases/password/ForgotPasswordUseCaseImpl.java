package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.password.ForgotPasswordUseCase;
import com.ticketoffice.backend.domain.usecases.password.GeneratePasswordResetTokenUseCase;
import jakarta.inject.Inject;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ForgotPasswordUseCaseImpl implements ForgotPasswordUseCase {

    private final MailSenderPort mailSenderPort;
    private final GeneratePasswordResetTokenUseCase generatePasswordResetTokenUseCase;

    @Inject
    public ForgotPasswordUseCaseImpl(
            MailSenderPort mailSenderPort,
            GeneratePasswordResetTokenUseCase generatePasswordResetTokenUseCase
    ) {
        this.mailSenderPort = mailSenderPort;
        this.generatePasswordResetTokenUseCase = generatePasswordResetTokenUseCase;
    }

    @Override
    public void accept(User user) {
        PasswordResetToken passwordResetToken = generatePasswordResetTokenUseCase.apply(user);
        sendEmail(passwordResetToken);
    }

    private void sendEmail(@NotNull PasswordResetToken passwordResetToken) {
        MailMessage mailMessage = new MailMessage(
                MailTemplates.FORGOT_PASSWORD,
                passwordResetToken.email(),
                Map.of(
                        "username", passwordResetToken.username(),
                        "reset-url", generateUrlToForgotPassword(passwordResetToken.tokenHash())
                ));

        mailSenderPort.sendEmail(mailMessage);
    }

    private String generateUrlToForgotPassword(String token) {
        return EmailConstants.FRONTEND_URL + String.format(EmailConstants.PATH_TO_RESET_PASSWORD, token);
    }
}
