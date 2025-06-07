package com.ticketoffice.backend.application.usecases.sales;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.exception.TicketValidatedPreviouslyException;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.sales.GetSaleByIdUseCase;
import com.ticketoffice.backend.domain.usecases.sales.UpdateSaleUseCase;
import com.ticketoffice.backend.domain.usecases.sales.ValidateSaleByIdUseCase;
import org.springframework.stereotype.Service;

@Service
public class ValidateSaleByIdUseCaseImpl implements ValidateSaleByIdUseCase {

    private final GetSaleByIdUseCase getSaleByIdUseCase;
    private final UpdateSaleUseCase updateSaleUseCase;

    public ValidateSaleByIdUseCaseImpl(
            GetSaleByIdUseCase getSaleByIdUseCase,
            UpdateSaleUseCase updateSaleUseCase
    ) {
        this.getSaleByIdUseCase = getSaleByIdUseCase;
        this.updateSaleUseCase = updateSaleUseCase;
    }

    @Override
    public void accept(String id)
            throws ResourceDoesntExistException, TicketValidatedPreviouslyException, ErrorOnPersistDataException
    {
        Sale sale = getSaleByIdUseCase.apply(id)
                .orElseThrow(() -> new ResourceDoesntExistException("Sale not found"));

        if (Boolean.TRUE.equals(sale.validated())) {
            throw new TicketValidatedPreviouslyException("Ticket has already been validated");
        }

        Sale copyWithUpdatedValidated = sale.getCopyWithUpdatedValidated(true);
        updateSaleUseCase.apply(id, copyWithUpdatedValidated)
                .orElseThrow(() -> new ErrorOnPersistDataException("Sale could not be updated"));
    }
}
