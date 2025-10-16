package com.ticketoffice.backend.infra.adapters.in.controller;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import io.javalin.http.Context;

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
     * @param context The context of the request.
     * @param allowedRoles The allowed roles.
     * @throws UnauthorizedUserException If the user does not have one of the allowed roles.
     */
    public void validateUserRole(Context context, List<UserRole> allowedRoles) throws UnauthorizedUserException {
        getAuthenticatedUserUseCase.apply(context)
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
    public void validateThatUserIsSeller(Context context) throws UnauthorizedUserException {
        validateUserRole(context, List.of(UserRole.SELLER));
    }

    public void validateThatUserIsAdmin(Context context) throws UnauthorizedUserException {
        validateUserRole(context, List.of());
    }

    public void validateThatUserIsLogged(Context context) throws UnauthorizedUserException {
        validateUserRole(context, List.of(UserRole.values()));
    }
}
