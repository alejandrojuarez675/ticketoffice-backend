package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.UserToken;
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
        UserToken passwordResetUserToken = generatePasswordResetTokenUseCase.apply(user);
        sendEmail(passwordResetUserToken);
    }

    private void sendEmail(@NotNull UserToken passwordResetUserToken) {
        MailMessage mailMessage = new MailMessage(
                MailTemplates.FORGOT_PASSWORD,
                passwordResetUserToken.email(),
                Map.of(
                        "username", passwordResetUserToken.username(),
                        "reset-url", generateUrlToForgotPassword(passwordResetUserToken.tokenHash())
                ));

        mailSenderPort.sendEmail(mailMessage);
    }

    private String generateUrlToForgotPassword(String token) {
        return EmailConstants.FRONTEND_URL + String.format(EmailConstants.PATH_TO_RESET_PASSWORD, token);
    }
}
