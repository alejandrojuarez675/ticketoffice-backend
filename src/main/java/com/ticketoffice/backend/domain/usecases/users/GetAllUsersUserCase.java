package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import java.util.List;

public interface GetAllUsersUserCase {
    /**
     * Retrieves all users.
     *
     * @return a list of all users.
     */
    List<User> getAllUsers();
}
