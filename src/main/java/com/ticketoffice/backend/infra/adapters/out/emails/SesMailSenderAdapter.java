package com.ticketoffice.backend.infra.adapters.out.emails;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.ses.SesAsyncClient;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailRequest;

@Singleton
public class SesMailSenderAdapter implements MailSenderPort {
    private static final Logger log = LoggerFactory.getLogger(SesMailSenderAdapter.class);
    
    private final SesAsyncClient sesClient;
    private final String senderEmail;
    private final ObjectMapper objectMapper;

    @Inject
    public SesMailSenderAdapter(SesAsyncClient sesClient, String senderEmail) {
        this.sesClient = sesClient;
        this.senderEmail = senderEmail;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<Void> sendEmail(MailMessage mailMessage) {
        log.debug("Sending email using template: {} to: {}", 
                 mailMessage.getTemplateName(), 
                 mailMessage.getTo());

        String templateDataJson;
        try {
             templateDataJson = objectMapper.writeValueAsString(mailMessage.getTemplateData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var request = SendTemplatedEmailRequest.builder()
            .source(senderEmail)
            .destination(b -> b.toAddresses(mailMessage.getTo()))
            .template(mailMessage.getTemplateName().getTemplate())
            .templateData(templateDataJson)
            .build();

        return sesClient.sendTemplatedEmail(request)
            .thenAccept(response -> 
                log.info("Email sent successfully! Message ID: {}", response.messageId()))
            .exceptionally(ex -> {
                log.error("Error sending email", ex);
                throw new RuntimeException("Failed to send email", ex);
            });
    }
}
