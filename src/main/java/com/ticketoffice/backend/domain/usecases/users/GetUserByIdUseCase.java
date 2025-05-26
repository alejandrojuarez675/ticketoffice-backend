package com.ticketoffice.backend.domain.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import java.util.Optional;

public interface GetUserByIdUseCase {

    Optional<User> findById(String id);
}
