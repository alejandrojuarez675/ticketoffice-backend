package com.ticketoffice.backend.application.usecases.emails;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SendConfirmationEmailToBuyerUseCaseImpl implements SendConfirmationEmailToBuyerUseCase {

    private final MailSenderPort mailSender;

    @Inject
    public SendConfirmationEmailToBuyerUseCaseImpl(MailSenderPort mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void accept(Sale sale, Event event) {
        mailSender.sendEmail(new MailMessage(
                MailTemplates.CONFIRMATION_EMAIL_TEMPLATE,
                sale.mainEmail(),
                Map.of(
                        "buyer-name", sale.buyer().getFirst().name(),
                        "event-name", event.title(),
                        "event-date", event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        "event-location", event.location().name()
                )
        )).join();
    }
}
