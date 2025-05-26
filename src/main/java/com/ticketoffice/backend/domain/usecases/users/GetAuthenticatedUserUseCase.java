package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import java.util.Optional;

public interface GetAuthenticatedUserUseCase {
    /**
     * Retrieves the currently authenticated user.
     *
     * @return The authenticated user.
     */
    Optional<User> getAuthenticatedUser();
}
