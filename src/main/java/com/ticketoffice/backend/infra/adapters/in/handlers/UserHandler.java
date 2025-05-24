package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.users.GetAllUsersUserCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {

    private final GetAllUsersUserCase getAllUsersUserCase;

    public UserHandler(GetAllUsersUserCase getAllUsersUserCase) {
        this.getAllUsersUserCase = getAllUsersUserCase;
    }

    public UserResponse getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        return new UserResponse(
            currentUser.getId(),
            currentUser.getUsername(),
            currentUser.getEmail(),
            currentUser.getRole().stream().map(Enum::name).toList()
        );
    }

    public List<UserResponse> getAllUsers() {
        return getAllUsersUserCase.getAllUsers().stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().stream().map(Enum::name).toList()
            ))
            .toList();
    }
}
