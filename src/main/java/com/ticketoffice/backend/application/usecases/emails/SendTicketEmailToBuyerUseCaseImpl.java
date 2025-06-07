package com.ticketoffice.backend.application.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
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

    @Value("${baseurl.frontend}")
    private String frontendUrl;

    @Value("${url.frontend.confirmation}")
    private String pathToConfirmationPage;

    public SendTicketEmailToBuyerUseCaseImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void accept(Sale sale, Event event) {
        emailService.sendEmail(
                createConfirmationEmailContent(sale, event),
                List.of(sale.mainEmail()),
                from,
                "Felicitaciones compraste un sale para %s".formatted(event.title())
        );
    }

    private String createConfirmationEmailContent(Sale sale, Event event) {
        return """
                <html>
                <body>
                <p>Hi, <b>%s</b></p>
                <p>Thanks for your purchase. Here is your sale.</p>
                <p><b>Event</b>: %s</p>
                <p><b>Date</b>: %s at %s</p>
                <p><b>Location</b>: %s</p>
                <p><b>Ticket</b>: %s</p>
                <p>QR Code:</p>
                <p>%s</p>
                </body>
                </html>
                """.formatted(
                sale.buyer().getFirst().name(),
                sale.eventId(),
                event.title(),
                event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                event.location().name(),
                sale.id(),
                generateSvgOfQrCode(sale.id())
        );
    }

    private String generateSvgOfQrCode(String id) {
        return """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <image href="https://api.qrserver.com/v1/create-qr-code/?data=%s" x="0" y="0" width="100" height="100" />
                </svg>
                """.formatted(getUrlToConfirmTicket(id));
    }

    private String getUrlToConfirmTicket(String id) {
        return frontendUrl + pathToConfirmationPage.replace("{id}", id);
    }
}
