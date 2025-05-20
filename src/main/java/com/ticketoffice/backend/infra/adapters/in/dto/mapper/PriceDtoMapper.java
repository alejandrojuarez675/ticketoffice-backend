package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.TicketPrice;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.PriceDTO;

public class PriceDtoMapper {

    public static PriceDTO getFromPrice(TicketPrice price) {
        return new PriceDTO(
                price.id(),
                price.value(),
                price.currency(),
                price.type(),
                price.isFree()
        );
    }
}
