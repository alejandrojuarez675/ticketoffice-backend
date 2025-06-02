package com.ticketoffice.backend.application.usecases.tickets;

import com.ticketoffice.backend.application.utils.CheckoutSessionIdUtils;
import com.ticketoffice.backend.domain.ports.CheckoutSessionCache;
import com.ticketoffice.backend.domain.usecases.tickets.GetOnHoldTicketsStockUseCase;
import org.springframework.stereotype.Service;

@Service
public class GetOnHoldTicketsStockUseCaseImpl implements GetOnHoldTicketsStockUseCase {

    private final CheckoutSessionCache checkoutSessionCache;

    public GetOnHoldTicketsStockUseCaseImpl(CheckoutSessionCache checkoutSessionCache) {
        this.checkoutSessionCache = checkoutSessionCache;
    }

    @Override
    public Integer getOnHoldTicketsStock(String eventId, String ticketId) {
        return checkoutSessionCache.countKeysMatches(CheckoutSessionIdUtils.getCheckoutSessionPattern(eventId, ticketId));
    }
}
