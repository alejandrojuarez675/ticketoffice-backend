package com.ticketoffice.backend.application.usecases.emails;

import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmAccountEmail;
import com.ticketoffice.backend.domain.usecases.password.GeneratePasswordResetTokenUseCase;
import jakarta.inject.Inject;
import java.util.Map;

public class SendConfirmAccountEmailImpl implements SendConfirmAccountEmail {

    private final MailSenderPort mailSender;
    private final GeneratePasswordResetTokenUseCase generatePasswordResetTokenUseCase;

    @Inject
    public SendConfirmAccountEmailImpl(
            MailSenderPort mailSender,
            GeneratePasswordResetTokenUseCase generatePasswordResetTokenUseCase
    ) {
        this.mailSender = mailSender;
        this.generatePasswordResetTokenUseCase = generatePasswordResetTokenUseCase;
    }

    @Override
    public void accept(User user) {
        PasswordResetToken passwordResetToken = generatePasswordResetTokenUseCase.apply(user);

        MailMessage mailMessage = new MailMessage(
                MailTemplates.CONFIRMATION_USER_ACCOUNT_TEMPLATE,
                user.getEmail(),
                Map.of(
                        "username", user.getUsername(),
                        "validation-url", EmailConstants.FRONTEND_URL +
                                String.format(EmailConstants.PATH_TO_CONFIRM_ACCOUNT, passwordResetToken.tokenHash())
                )
        );
        mailSender.sendEmail(mailMessage);
    }
}
