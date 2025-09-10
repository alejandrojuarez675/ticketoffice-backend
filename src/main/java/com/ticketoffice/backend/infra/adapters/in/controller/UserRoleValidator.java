package com.ticketoffice.backend.infra.adapters.in.controller;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleValidator {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Inject
    public UserRoleValidator(GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    /**
     * Validates that the authenticated user has one of the allowed roles.
     * <p>
     * <b>The ADMIN role is always allowed.</b>
     *
     * @param allowedRoles The allowed roles.
     * @throws UnauthorizedUserException If the user does not have one of the allowed roles.
     */
    public void validateUserRole(List<UserRole> allowedRoles) throws UnauthorizedUserException {
        getAuthenticatedUserUseCase.get()
                .filter(user -> {
                    List<UserRole> allowedRolesAndAdmin = new ArrayList<>();
                    allowedRolesAndAdmin.add(UserRole.ADMIN);
                    allowedRolesAndAdmin.addAll(allowedRoles);

                    return user.getRole().stream()
                            .anyMatch(allowedRolesAndAdmin::contains);
                })
                .orElseThrow(() -> new UnauthorizedUserException("You do not have permission to access this user"));
    }

    /**
     * Validates that the authenticated user is a seller.
     * <p>
     * <b>The ADMIN role is always allowed.</b>
     *
     * @throws UnauthorizedUserException If the user is not a seller.
     */
    public void validateThatUserIsSeller() throws UnauthorizedUserException {
        validateUserRole(List.of(UserRole.SELLER));
    }

    public void validateThatUserIsAdmin() throws UnauthorizedUserException {
        validateUserRole(List.of());
    }

    public void validateThatUserIsLogged() throws UnauthorizedUserException {
        validateUserRole(List.of(UserRole.values()));
    }
}
