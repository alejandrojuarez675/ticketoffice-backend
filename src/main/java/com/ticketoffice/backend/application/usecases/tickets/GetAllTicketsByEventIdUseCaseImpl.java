package com.ticketoffice.backend.application.usecases.tickets;

import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.TicketRepository;
import com.ticketoffice.backend.domain.usecases.tickets.GetAllTicketsByEventIdUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllTicketsByEventIdUseCaseImpl implements GetAllTicketsByEventIdUseCase {

    private final TicketRepository ticketRepository;

    public GetAllTicketsByEventIdUseCaseImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> apply(String eventId) {
        return ticketRepository.findByEventId(eventId);
    }
}
