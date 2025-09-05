package com.ticketoffice.backend.application.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.users.GetAllUsersUserCase;
import java.util.List;

public class GetAllUsersUserCaseImpl implements GetAllUsersUserCase {

    private final UserRepository userRepository;

    public GetAllUsersUserCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> get() {
        return userRepository.findAll();
    }
}
