package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.CheckoutSession;
import com.ticketoffice.backend.domain.models.Purchase;
import com.ticketoffice.backend.infra.adapters.in.dto.request.BuyTicketsRequest;

public class PurchaseMapper {
    public static Purchase getPurchase(CheckoutSession session, BuyTicketsRequest request) {
        return new Purchase(
                null,
                session.getEventId(),
                session.getPriceId(),
                session.getQuantity(),
                request.buyer().stream().map(BuyerMapper::get).toList(),
                request.mainEmail()
        );
    }
}
