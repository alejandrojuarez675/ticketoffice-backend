package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import java.util.Optional;

public interface UpdateOrganizerDataOnUserUseCase {
    Optional<User> update(User user, Organizer organizer);
}
