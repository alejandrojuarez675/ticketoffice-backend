package com.ticketoffice.backend.application.usecases.sales;

import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.sales.UpdateSaleUseCase;
import java.util.Optional;

public class UpdateSaleUseCaseImpl implements UpdateSaleUseCase {

    private final SaleRepository saleRepository;

    public UpdateSaleUseCaseImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Optional<Sale> apply(String id, Sale sale) {
        return saleRepository.update(id, sale);
    }
}
