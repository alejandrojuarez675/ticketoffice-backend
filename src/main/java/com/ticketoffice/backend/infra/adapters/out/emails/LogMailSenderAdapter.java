package com.ticketoffice.backend.infra.adapters.out.emails;

import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogMailSenderAdapter implements MailSenderPort {

    private final Logger logger = LoggerFactory.getLogger(LogMailSenderAdapter.class);

    private final Map<String, String> templates;

    public LogMailSenderAdapter() {
        this.templates = Map.of(
                "confirm-email-template", "Confirm email template",
                "send-ticket", "Send ticket template"
        );
    }

    @Override
    public CompletableFuture<Void> sendEmail(MailMessage mailMessage) {
        logger.info("Sending email to {} with template {}", mailMessage.getTo(), templates.get(mailMessage.getTemplateName()));
        return null;
    }
}
