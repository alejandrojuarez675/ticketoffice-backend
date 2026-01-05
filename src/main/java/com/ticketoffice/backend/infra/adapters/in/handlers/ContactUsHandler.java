package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.usecases.notifications.NotificateToAdminUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.request.ContactUsRequest;
import jakarta.inject.Inject;

public class ContactUsHandler {

    private final NotificateToAdminUseCase notificateToAdminUseCase;

    @Inject
    public ContactUsHandler(NotificateToAdminUseCase notificateToAdminUseCase) {
        this.notificateToAdminUseCase = notificateToAdminUseCase;
    }

    public void handle(ContactUsRequest contactUsRequest) {
        var subject = String.format("[Contact Us] %s", contactUsRequest.subject());
        var message = String.format("%s - %s - %s", contactUsRequest.name(), contactUsRequest.email(), contactUsRequest.message());
        notificateToAdminUseCase.accept(subject, message);
    }
}
