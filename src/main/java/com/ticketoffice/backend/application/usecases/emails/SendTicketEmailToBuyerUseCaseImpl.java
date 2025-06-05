package com.ticketoffice.backend.application.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.EmailService;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendTicketEmailToBuyerUseCaseImpl implements SendTicketEmailToBuyerUseCase {

    private final EmailService emailService;

    @Value("${email.no-reply-email}")
    private String from;

    public SendTicketEmailToBuyerUseCaseImpl(EmailService emailService) {
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
                <p>Thanks for your purchase. Here is your ticket.</p>
                <p>Event: %s</p>
                <p>Date: %s at %s</p>
                <p>Location: %s</p>
                <p>Ticket ID: %s</p>
                <p>QR Code: %s</p>
                </body>
                </html>
                """.formatted(
                ticket.buyer().getFirst().name(),
                ticket.eventId(),
                event.title(),
                event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                event.location().name(),
                ticket.id(),
                generateSvgOfQrCode(ticket.id())
        );
    }

    private String generateSvgOfQrCode(String id) {
        return """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <image href="https://api.qrserver.com/v1/create-qr-code/?data=%s" x="0" y="0" width="100" height="100" />
                </svg>
                """.formatted(id);
    }
}
