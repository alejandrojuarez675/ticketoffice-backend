package com.ticketoffice.backend.domain.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Ticket;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SendTicketEmailToBuyerUseCase extends UseCase, BiConsumer<Ticket, Event> {
}
