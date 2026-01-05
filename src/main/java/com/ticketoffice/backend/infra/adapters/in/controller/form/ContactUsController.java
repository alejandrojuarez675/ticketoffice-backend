package com.ticketoffice.backend.infra.adapters.in.controller.form;

import com.ticketoffice.backend.infra.adapters.in.controller.CustomController;
import com.ticketoffice.backend.infra.adapters.in.dto.request.ContactUsRequest;
import com.ticketoffice.backend.infra.adapters.in.handlers.ContactUsHandler;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class ContactUsController implements CustomController {

    private final ContactUsHandler contactUsHandler;

    @Inject
    public ContactUsController(ContactUsHandler contactUsHandler) {
        this.contactUsHandler = contactUsHandler;
    }

    @Override
    public void registeredRoutes(Javalin app) {
        app.post("/api/public/v1/form/contact-us", this::handle);
    }

    private void handle(@NotNull Context ctx) {
        ContactUsRequest contactUsRequest = ctx.bodyAsClass(ContactUsRequest.class);
        contactUsHandler.handle(contactUsRequest);
        ctx.status(HttpStatus.CREATED);
    }
}
