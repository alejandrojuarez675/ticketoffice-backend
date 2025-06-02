package com.ticketoffice.backend.domain.usecases.organizer;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface GetOrganizerByUserIdUseCase extends UseCase, Function<String, Optional<Organizer>> {
}
