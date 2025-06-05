package com.ticketoffice.backend.application.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.EmailService;
import com.ticketoffice.backend.domain.usecases.emails.SendConfirmationEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendConfirmationEmailToBuyerUseCaseImpl implements SendConfirmationEmailToBuyerUseCase {

    private final EmailService emailService;

    @Value("${email.no-reply-email}")
    private String from;

    public SendConfirmationEmailToBuyerUseCaseImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void accept(Ticket ticket, Event event) {
        emailService.sendEmail(
                createConfirmationEmailContent(ticket, event),
                List.of(ticket.mainEmail()),
                from);
    }

    private String createConfirmationEmailContent(Ticket ticket, Event event) {
        return """
                <html>
                <body>
                <p>Hi, <b>%s</b></p>
                <p>Thanks for your purchase. Here is your confirmation email.</p>
                <p>Event: %s</p>
                <p>Date: %s at %s</p>
                <p>Location: %s</p>
                </body>
                </html>
                """.formatted(
                        ticket.buyer().getFirst().name(),
                        ticket.eventId(),
                        event.title(),
                        event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        event.location().name()
                );
    }
}
