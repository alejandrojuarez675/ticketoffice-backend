package com.ticketoffice.backend.application.usecases.tickets;

import com.google.inject.Inject;
import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.tickets.GetOnHoldTicketsStockUseCase;

public class GetOnHoldTicketsStockUseCaseImpl implements GetOnHoldTicketsStockUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    @Inject
    public GetOnHoldTicketsStockUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public Integer apply(String eventId, String ticketId) {
        return checkoutSessionCache.countKeysMatches(CheckoutSessionIdUtils.getCheckoutSessionPattern(eventId, ticketId));
    }
}
