package com.ticketoffice.backend.infra.adapters.in.controller;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.dto.TestEmailRequest;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class TestEmailController implements CustomController {
    private final MailSenderPort mailSenderPort;

    @Inject
    public TestEmailController(MailSenderPort mailSenderPort) {
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post("/test-email", this::handle);
    }

    public void handle(@NotNull Context ctx) {
        TestEmailRequest request = ctx.bodyAsClass(TestEmailRequest.class);
        
        // Validate required fields
        if (request.templateName() == null || request.to() == null || request.templateData() == null) {
            ctx.status(400).json(Map.of("error", "Missing required fields"));
            return;
        }

        MailMessage mailMessage = new MailMessage(
            request.templateName(),
            request.to(),
            request.templateData()
        );

        mailSenderPort.sendEmail(mailMessage);
        ctx.status(202).json(Map.of("message", "Email sent successfully"));
    }
}
