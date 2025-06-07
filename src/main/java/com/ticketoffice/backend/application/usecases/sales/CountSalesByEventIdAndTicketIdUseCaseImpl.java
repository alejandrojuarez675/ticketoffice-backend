package com.ticketoffice.backend.application.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.sales.CountSalesByEventIdAndTicketIdUseCase;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class CountSalesByEventIdAndTicketIdUseCaseImpl implements CountSalesByEventIdAndTicketIdUseCase {

    private final SaleRepository saleRepository;

    public CountSalesByEventIdAndTicketIdUseCaseImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Integer apply(String eventId, String ticketId) {
        Predicate<Sale> predicate = t -> t.eventId().equals(eventId) && t.ticketId().equals(ticketId);
        return saleRepository.count(predicate);
    }
}
