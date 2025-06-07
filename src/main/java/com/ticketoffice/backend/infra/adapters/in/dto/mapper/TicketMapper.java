package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;

public class TicketMapper {
    public static Sale getTicketFromBuyTickets(CheckoutSession session, BuyTicketsRequest request) {
        return new Sale(
                null,
                session.getEventId(),
                session.getPriceId(),
                session.getQuantity(),
                session.getPrice(),
                request.buyer().stream().map(BuyerMapper::get).toList(),
                request.mainEmail(),
                Boolean.FALSE
        );
    }
}
