package com.ticketoffice.backend.application.usecases.notifications;

import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.notifications.NotificateToAdminUseCase;
import jakarta.inject.Inject;
import java.util.Map;

public class NotificateToAdminUseCaseImpl implements NotificateToAdminUseCase {

    private final MailSenderPort mailSenderPort;

    @Inject
    public NotificateToAdminUseCaseImpl(MailSenderPort mailSenderPort) {
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public void accept(String subject, String msg) {
        EmailConstants.ADMIN_EMAIL.stream()
                .map(email -> generateMailMessage(email, subject, msg))
                .forEach(mailSenderPort::sendEmail);
    }

    private MailMessage generateMailMessage(String email, String subject, String msg) {
        return new MailMessage(
                MailTemplates.NOTIFICATION_TO_ADMIN,
                email,
                Map.of("subject", subject, "msg", msg)
        );
    }
}
