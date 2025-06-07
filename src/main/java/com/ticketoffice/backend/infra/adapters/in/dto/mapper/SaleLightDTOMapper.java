package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.infra.adapters.in.dto.response.tickets.SaleLightDTO;

public class SaleLightDTOMapper {
    public static SaleLightDTO getFromSale(Sale sale, Event event) {
        var buyer = sale.buyer().getFirst();

        return event.tickets().stream()
                .filter(t -> t.id().equals(sale.ticketId()))
                .findFirst()
                .map(ticketPrice -> new SaleLightDTO(
                        sale.id(),
                        buyer.name(),
                        buyer.lastName(),
                        buyer.email(),
                        ticketPrice.type(),
                        sale.quantity() * ticketPrice.value(),
                        sale.validated()
                )).orElse(null);

    }
}
