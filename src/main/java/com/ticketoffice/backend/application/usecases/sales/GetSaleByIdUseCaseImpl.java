package com.ticketoffice.backend.application.usecases.sales;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.ports.SaleRepository;
import com.ticketoffice.backend.domain.usecases.sales.GetSaleByIdUseCase;
import java.util.Optional;

public class GetSaleByIdUseCaseImpl implements GetSaleByIdUseCase {

    private final SaleRepository saleRepository;

    @Inject
    public GetSaleByIdUseCaseImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Optional<Sale> apply(String s) {
        return saleRepository.getById(s);
    }
}
