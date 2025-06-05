package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;

public class TicketMapper {
    public static Ticket getTicketFromBuyTickets(CheckoutSession session, BuyTicketsRequest request) {
        return new Ticket(
                null,
                session.getEventId(),
                session.getPriceId(),
                session.getQuantity(),
                request.buyer().stream().map(BuyerMapper::get).toList(),
                request.mainEmail()
        );
    }
}
