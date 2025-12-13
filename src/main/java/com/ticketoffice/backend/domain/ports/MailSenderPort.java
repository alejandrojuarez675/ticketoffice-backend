package com.ticketoffice.backend.domain.ports;

import java.util.concurrent.CompletableFuture;

import com.ticketoffice.backend.domain.models.MailMessage;

public interface MailSenderPort {
    CompletableFuture<Void> sendEmail(MailMessage mailMessage);
}
