package com.ticketoffice.backend.application.usecases.users;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.users.GetUserByIdUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {

    private final UserRepository userRepository;

    public GetUserByIdUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> apply(String s) {
        return userRepository.getById(s);
    }
}
