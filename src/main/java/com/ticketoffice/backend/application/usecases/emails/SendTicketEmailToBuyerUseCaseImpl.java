package com.ticketoffice.backend.application.usecases.emails;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.utils.QrUtils;
import com.ticketoffice.backend.domain.constants.EmailConstants;
import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.usecases.emails.SendTicketEmailToBuyerUseCase;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SendTicketEmailToBuyerUseCaseImpl implements SendTicketEmailToBuyerUseCase {

    private final MailSenderPort mailSenderPort;

    @Inject
    public SendTicketEmailToBuyerUseCaseImpl(MailSenderPort mailSenderPort) {
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public void accept(Sale sale, Event event) {
        mailSenderPort.sendEmail(new MailMessage(
                MailTemplates.SEND_TICKET,
                sale.mainEmail(),
                Map.of(
                        "buyer-name", sale.buyer().getFirst().name(),
                        "event-id", sale.eventId(),
                        "event-name", event.title(),
                        "event-date", event.date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        "event-location", event.location().name(),
                        "sale-id", sale.id(),
                        "qr-code", QrUtils.generateSvgOfQrCode(getUrlToConfirmTicket(event.id(), sale.id()))
                )
        )).join();
    }

    private String getUrlToConfirmTicket(String eventId, String saleId) {
        return EmailConstants.FRONTEND_URL
                + EmailConstants.PATH_TO_CONFIRMATION_PAGE.replace("{eventId}", eventId).replace("{saleId}", saleId);
    }
}
