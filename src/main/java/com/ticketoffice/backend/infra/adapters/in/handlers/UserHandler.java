package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.users.GetAllUsersUserCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {

    private final GetAllUsersUserCase getAllUsersUserCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final IsAnAdminUserUseCase isAnAdminUserUseCase;

    public UserHandler(
            GetAllUsersUserCase getAllUsersUserCase,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, IsAnAdminUserUseCase isAnAdminUserUseCase
    ) {
        this.getAllUsersUserCase = getAllUsersUserCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.isAnAdminUserUseCase = isAnAdminUserUseCase;
    }

    public UserResponse getAuthenticatedUser() {
        return getAuthenticatedUserUseCase.getAuthenticatedUser()
                .map(this::createUserResponse)
                .orElse(null);
    }

    private UserResponse createUserResponse(User currentUser) {
        return new UserResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole().stream().map(Enum::name).toList()
        );
    }

    public List<UserResponse> getAllUsers() throws UnauthorizedUserException {
        if (!isAnAdminUserUseCase.isAdmin()) {
            throw new UnauthorizedUserException("You do not have permission to access this user");
        }

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
