package com.ticketoffice.backend.application.usecases.users;

import com.ticketoffice.backend.domain.models.Organizer;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.users.UpdateOrganizerDataOnUserUseCase;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrganizerDataOnUserUseCaseImpl implements UpdateOrganizerDataOnUserUseCase {

    private final UserRepository userRepository;

    public UpdateOrganizerDataOnUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> apply(User user, Organizer organizer) {
        User updatedUser = updateUserData(user, organizer);
        return userRepository.update(updatedUser.getId(), updatedUser);
    }

    private User updateUserData(User user, Organizer organizer) {
        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                organizer
        );
    }
}
