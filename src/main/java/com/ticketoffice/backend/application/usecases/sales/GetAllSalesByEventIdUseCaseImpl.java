package com.ticketoffice.backend.application.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.sales.GetAllSalesByEventIdUseCase;
import java.util.List;

public class GetAllSalesByEventIdUseCaseImpl implements GetAllSalesByEventIdUseCase {

    private final SaleRepository saleRepository;

    public GetAllSalesByEventIdUseCaseImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> apply(String eventId) {
        return saleRepository.findByEventId(eventId);
    }
}
