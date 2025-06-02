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
                price.isFree(),
                price.stock()
        );
    }

    public static TicketPrice getFromPriceDTO(PriceDTO priceDTO) {
        return new TicketPrice(
                priceDTO.id(),
                priceDTO.value(),
                priceDTO.currency(),
                priceDTO.type(),
                priceDTO.isFree(),
                priceDTO.stock()
        );
    }
}
