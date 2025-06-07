package com.ticketoffice.backend.infra.adapters.in.dto.mapper;

import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.TicketDTO;

public class TicketDtoMapper {

    public static TicketDTO getFromTicket(Ticket price) {
        return new TicketDTO(
                price.id(),
                price.value(),
                price.currency(),
                price.type(),
                price.isFree(),
                price.stock()
        );
    }

    public static Ticket getFromTicketDTO(TicketDTO ticketDTO) {
        return new Ticket(
                ticketDTO.id(),
                ticketDTO.value(),
                ticketDTO.currency(),
                ticketDTO.type(),
                ticketDTO.isFree(),
                ticketDTO.stock()
        );
    }
}
