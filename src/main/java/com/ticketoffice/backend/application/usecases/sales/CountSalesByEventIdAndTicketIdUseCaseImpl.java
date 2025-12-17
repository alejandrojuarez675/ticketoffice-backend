package com.ticketoffice.backend.application.usecases.sales;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.sales.CountSalesByEventIdAndTicketIdUseCase;

/**
 * Implementation of the CountSalesByEventIdAndTicketIdUseCase interface.
 * This use case counts the number of sales for a specific event and ticket.
 */
public class CountSalesByEventIdAndTicketIdUseCaseImpl implements CountSalesByEventIdAndTicketIdUseCase {

    private final SaleRepository saleRepository;

    @Inject
    public CountSalesByEventIdAndTicketIdUseCaseImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Integer apply(String eventId, String ticketId) {
        return saleRepository.countByEventIdAndTicketId(eventId, ticketId);
    }
}
