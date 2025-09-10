package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.usecases.organizer.GetOrganizerByUserIdUseCase;
import com.ticketoffice.backend.domain.usecases.users.GetAllUsersUserCase;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.mapper.OrganizerDtoMapper;
import com.ticketoffice.backend.infra.adapters.in.dto.response.UserResponse;
import com.ticketoffice.backend.infra.adapters.in.dto.shared.OrganizerDTO;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import java.util.List;

public class UserHandler {

    private final GetAllUsersUserCase getAllUsersUserCase;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final IsAnAdminUserUseCase isAnAdminUserUseCase;
    private final GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase;

    @Inject
    public UserHandler(
            GetAllUsersUserCase getAllUsersUserCase,
            GetAuthenticatedUserUseCase getAuthenticatedUserUseCase,
            IsAnAdminUserUseCase isAnAdminUserUseCase,
            GetOrganizerByUserIdUseCase getOrganizerByUserIdUseCase) {
        this.getAllUsersUserCase = getAllUsersUserCase;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.isAnAdminUserUseCase = isAnAdminUserUseCase;
        this.getOrganizerByUserIdUseCase = getOrganizerByUserIdUseCase;
    }

    public UserResponse getAuthenticatedUser() {
        return getAuthenticatedUserUseCase.get()
                .map(this::createUserResponse)
                .orElse(null);
    }

    private UserResponse createUserResponse(User currentUser) {
        OrganizerDTO organizerDTO = getOrganizerDTO(currentUser);

        return new UserResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getRole().stream().map(Enum::name).toList(),
                organizerDTO
        );
    }

    private OrganizerDTO getOrganizerDTO(User currentUser) {
        return getOrganizerByUserIdUseCase.apply(currentUser.getId())
                .map(OrganizerDtoMapper::getFromOrganizer)
                .orElse(null);
    }

    public List<UserResponse> getAllUsers() throws UnauthorizedUserException {
        if (!isAnAdminUserUseCase.getAsBoolean()) {
            throw new UnauthorizedUserException("You do not have permission to access this user");
        }

        return getAllUsersUserCase.get().stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().stream().map(Enum::name).toList(),
                getOrganizerDTO(user)
            ))
            .toList();
    }
}
