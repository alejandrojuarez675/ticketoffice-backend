package com.ticketoffice.backend.application.usecases.emails;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.utils.QrUtils;
import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.EmailService;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SendTicketEmailToBuyerUseCaseImpl implements SendTicketEmailToBuyerUseCase {

    private final EmailService emailService;

    @Inject
    public SendTicketEmailToBuyerUseCaseImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void accept(Sale sale, Event event) {
        emailService.sendEmail(
                createConfirmationEmailContent(sale, event),
                List.of(sale.mainEmail()),
                EmailConstants.FROM,
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
                QrUtils.generateSvgOfQrCode(getUrlToConfirmTicket(event.id(), sale.id()))
        );
    }

    private String getUrlToConfirmTicket(String eventId, String saleId) {
        return EmailConstants.FRONTEND_URL
                + EmailConstants.PATH_TO_CONFIRMATION_PAGE.replace("{eventId}", eventId).replace("{saleId}", saleId);
    }
}
