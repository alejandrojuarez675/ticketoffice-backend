package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.exception.NotAuthenticatedException;
import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.events.GetMyEventUseCase;
import com.ticketoffice.backend.domain.usecases.sales.GetAllSalesByEventIdUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SaleLightDTO;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SalesListResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.NotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesHandler {

    private final GetAllSalesByEventIdUseCase getAllSalesByEventIdUseCase;
    private final GetMyEventUseCase getMyEventUseCase;

    public SalesHandler(
            GetAllSalesByEventIdUseCase getAllSalesByEventIdUseCase,
            GetMyEventUseCase getMyEventUseCase
    ) {
        this.getAllSalesByEventIdUseCase = getAllSalesByEventIdUseCase;
        this.getMyEventUseCase = getMyEventUseCase;
    }

    public SalesListResponse getAllSalesByEventId(String eventId) throws NotAuthenticatedException, NotFoundException {
        Event event = getMyEventUseCase.apply(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        List<Sale> sales = getAllSalesByEventIdUseCase.apply(eventId);

        return new SalesListResponse(
            sales.stream()
                .map(sale -> {
                    var buyer = sale.buyer().getFirst();

                    return event.prices().stream()
                            .filter(t -> t.id().equals(sale.ticketId()))
                            .findFirst()
                            .map(ticketPrice -> new SaleLightDTO(
                                    sale.id(),
                                    buyer.name(),
                                    buyer.lastName(),
                                    buyer.email(),
                                    ticketPrice.type(),
                                    sale.quantity() * ticketPrice.value()
                            ));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList())
        );
    }
}
