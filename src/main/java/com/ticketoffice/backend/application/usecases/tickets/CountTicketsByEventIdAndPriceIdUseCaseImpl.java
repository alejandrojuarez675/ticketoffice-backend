package com.ticketoffice.backend.application.usecases.tickets;

import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.ports.TicketRepository;
import com.ticketoffice.backend.domain.usecases.tickets.CountTicketsByEventIdAndPriceIdUseCase;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class CountTicketsByEventIdAndPriceIdUseCaseImpl implements CountTicketsByEventIdAndPriceIdUseCase {

    private final TicketRepository ticketRepository;

    public CountTicketsByEventIdAndPriceIdUseCaseImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Integer apply(String eventId, String ticketId) {
        Predicate<Ticket> predicate = t -> t.eventId().equals(eventId) && t.priceId().equals(ticketId);
        return ticketRepository.count(predicate);
    }
}
