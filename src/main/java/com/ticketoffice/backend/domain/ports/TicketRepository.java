package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.Ticket;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TicketRepository {
    Optional<Ticket> save(Ticket ticket);

    Integer count(Predicate<Ticket> predicate);

    List<Ticket> findByEventId(String eventId);
}
