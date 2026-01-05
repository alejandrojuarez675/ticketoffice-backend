package com.ticketoffice.backend.infra.adapters.in.controller.admin;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.dto.TestEmailRequest;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class TestEmailController implements CustomController {
    private final MailSenderPort mailSenderPort;
    private final IsAnAdminUserUseCase isAnAdminUserUseCase;

    @Inject
    public TestEmailController(
            MailSenderPort mailSenderPort,
            IsAnAdminUserUseCase isAnAdminUserUseCase
    ) {
        this.mailSenderPort = mailSenderPort;
        this.isAnAdminUserUseCase = isAnAdminUserUseCase;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post("/test-email", this::handle);
    }

    public void handle(@NotNull Context ctx) {
        if (!isAnAdminUserUseCase.apply(ctx.sessionAttribute("user"))) {
            ctx.status(403).json(Map.of("error", "Unauthorized"));
            return;
        }

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
