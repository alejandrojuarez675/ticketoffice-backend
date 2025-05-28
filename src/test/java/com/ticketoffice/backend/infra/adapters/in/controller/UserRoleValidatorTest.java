package com.ticketoffice.backend.infra.adapters.in.controller;

import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.infra.adapters.in.exception.UnauthorizedUserException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserRoleValidatorTest {

    private final User USER_SELLER = new User(
            "sfdsdf-0asdffsad-fasdfd",
            "test",
            "test",
            "test",
            List.of(UserRole.USER, UserRole.SELLER),
            null
    );

    private final User USER_BASIC = new User(
            "sfdsdf-0asdffsad-fasdfd",
            "test",
            "test",
            "test",
            List.of(UserRole.USER),
            null
    );

    private final User USER_ADMIN = new User(
            "sfdsdf-0asdffsad-fasdfd",
            "test",
            "test",
            "test",
            List.of(UserRole.ADMIN),
            null
    );

    @Test
    void validateUserRole() {
        UserRoleValidator userRoleValidator = new UserRoleValidator(() -> Optional.of(USER_BASIC));

        Assertions.assertDoesNotThrow(() -> userRoleValidator.validateUserRole(List.of(UserRole.USER)));
        Assertions.assertDoesNotThrow(() -> userRoleValidator.validateUserRole(List.of(UserRole.USER, UserRole.SELLER)));
        Assertions.assertDoesNotThrow(() -> userRoleValidator.validateUserRole(List.of(UserRole.USER, UserRole.ADMIN)));

        Assertions.assertThrows(
                UnauthorizedUserException.class,
                () -> userRoleValidator.validateUserRole(List.of(UserRole.ADMIN))
        );

        Assertions.assertThrows(
                UnauthorizedUserException.class,
                () -> userRoleValidator.validateUserRole(List.of(UserRole.SELLER))
        );

        Assertions.assertThrows(
                UnauthorizedUserException.class,
                () -> userRoleValidator.validateUserRole(List.of(UserRole.ADMIN, UserRole.SELLER))
        );
    }

    @Test
    void validateThatUserIsSeller() {
        UserRoleValidator userRoleValidatorBuyer = new UserRoleValidator(() -> Optional.of(USER_BASIC));

        Assertions.assertThrows(
                UnauthorizedUserException.class,
                userRoleValidatorBuyer::validateThatUserIsSeller
        );

        UserRoleValidator userRoleValidatorSeller = new UserRoleValidator(() -> Optional.of(USER_SELLER));
        Assertions.assertDoesNotThrow(userRoleValidatorSeller::validateThatUserIsSeller);

        UserRoleValidator userRoleValidatorAdmin = new UserRoleValidator(() -> Optional.of(USER_ADMIN));
        Assertions.assertDoesNotThrow(userRoleValidatorAdmin::validateThatUserIsSeller);
    }

    @Test
    void validateThatUserIsAdmin() {
        UserRoleValidator userRoleValidatorBuyer = new UserRoleValidator(() -> Optional.of(USER_BASIC));

        Assertions.assertThrows(
                UnauthorizedUserException.class,
                userRoleValidatorBuyer::validateThatUserIsAdmin
        );

        UserRoleValidator userRoleValidatorSeller = new UserRoleValidator(() -> Optional.of(USER_SELLER));
        Assertions.assertThrows(
                UnauthorizedUserException.class,
                userRoleValidatorSeller::validateThatUserIsAdmin
        );

        UserRoleValidator userRoleValidatorAdmin = new UserRoleValidator(() -> Optional.of(USER_ADMIN));
        Assertions.assertDoesNotThrow(userRoleValidatorAdmin::validateThatUserIsAdmin);

    }
}
