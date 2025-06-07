package com.ticketoffice.backend.domain.usecases.emails;

import com.ticketoffice.backend.domain.models.Event;
import com.ticketoffice.backend.domain.models.Sale;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.function.BiConsumer;

public interface SendConfirmationEmailToBuyerUseCase extends UseCase, BiConsumer<Sale, Event> {
}
