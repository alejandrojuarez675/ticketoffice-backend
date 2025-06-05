package com.ticketoffice.backend.domain.usecases.tickets;

import com.ticketoffice.backend.domain.models.Ticket;

import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface GetAllTicketsByEventIdUseCase extends UseCase, Function<String, List<Ticket>> {
}
