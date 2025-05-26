package com.ticketoffice.backend.domain.usecases.users;

public interface IsAnAdminUserUseCase {
    /**
     * Checks if the authenticated user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     */
    boolean isAdmin();
}
