package com.ticketoffice.backend.application.usecases.emails;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.EmailService;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SendConfirmationEmailToBuyerUseCaseImpl implements SendConfirmationEmailToBuyerUseCase {

    private final EmailService emailService;

//    @Value("${email.no-reply-email}")
    private String from;

    @Inject
    public SendConfirmationEmailToBuyerUseCaseImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void accept(Sale sale, Event event) {
        emailService.sendEmail(
                createConfirmationEmailContent(sale, event),
                List.of(sale.mainEmail()),
                from,
                "Confirmación de compra");
    }

    private String createConfirmationEmailContent(Sale sale, Event event) {
        return """
                <html>
                <body>
                <p>Hi, <b>%s</b></p>
                <p>Thanks for your purchase. Here is your confirmation email.</p>
                <p>Event: %s</p>
                <p>Date: %s</p>
                <p>Location: %s</p>
                </body>
                </html>
                """.formatted(
                        sale.buyer().getFirst().name(),
                        event.title(),
                        event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        event.location().name()
                );
    }
}
