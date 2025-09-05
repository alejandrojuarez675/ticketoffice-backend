package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.ErrorOnPersistDataException;
import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.exception.ResourceDoesntExistException;
import com.ticketoffice.backend.domain.exception.TicketValidatedPreviouslyException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.sales.GetAllSalesByEventIdUseCase;
import com.ticketoffice.backend.domain.usecases.sales.ValidateSaleByIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.SalesListResponseMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.BadRequestException;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.List;

public class SalesHandler {

    private final GetAllSalesByEventIdUseCase getAllSalesByEventIdUseCase;
    private final GetMyEventUseCase getMyEventUseCase;
    private final ValidateSaleByIdUseCase validateSaleByIdUseCase;

    public SalesHandler(
            GetAllSalesByEventIdUseCase getAllSalesByEventIdUseCase,
            GetMyEventUseCase getMyEventUseCase,
            ValidateSaleByIdUseCase validateSaleByIdUseCase
    ) {
        this.getAllSalesByEventIdUseCase = getAllSalesByEventIdUseCase;
        this.getMyEventUseCase = getMyEventUseCase;
        this.validateSaleByIdUseCase = validateSaleByIdUseCase;
    }

    public SalesListResponse getAllSalesByEventId(String eventId) throws NotAuthenticatedException, NotFoundException {
        Event event = getMyEventUseCase.apply(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        List<Sale> sales = getAllSalesByEventIdUseCase.apply(eventId);

        return SalesListResponseMapper.toResponse(sales, event);
    }

    public void validateSale(String saleId, String eventId)
            throws NotAuthenticatedException, NotFoundException, BadRequestException {
        getMyEventUseCase.apply(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        try {
            validateSaleByIdUseCase.accept(saleId);
        } catch (ResourceDoesntExistException e) {
            throw new NotFoundException(e.getMessage());
        } catch (TicketValidatedPreviouslyException e) {
            throw new BadRequestException(e.getMessage());
        } catch (ErrorOnPersistDataException e) {
            throw new RuntimeException(e);
        }
    }
}
