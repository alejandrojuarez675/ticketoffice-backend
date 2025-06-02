package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.UseCase;
import java.util.Optional;
import java.util.function.BiFunction;

@FunctionalInterface
public interface UpdateOrganizerDataOnUserUseCase extends UseCase, BiFunction<User, Organizer, Optional<User>> {
}
