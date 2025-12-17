package com.ticketoffice.backend.infra.adapters.out.emails;

import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogMailSenderAdapter implements MailSenderPort {

    private final Logger logger = LoggerFactory.getLogger(LogMailSenderAdapter.class);

    @Override
    public CompletableFuture<Void> sendEmail(MailMessage mailMessage) {
        logger.info("Sending email to {} with template {}", mailMessage.getTo(), mailMessage.getTemplateName());
        return CompletableFuture.completedFuture(null);
    }
}
