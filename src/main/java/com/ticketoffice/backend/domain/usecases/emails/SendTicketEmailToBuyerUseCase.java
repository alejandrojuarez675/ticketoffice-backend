package com.ticketoffice.backend.domain.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SendTicketEmailToBuyerUseCase extends UseCase, BiConsumer<Sale, Event> {
}
