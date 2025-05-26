package com.ticketoffice.backend.application.usecases.users;

import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import org.springframework.stereotype.Service;

@Service
public class IsAnAdminUserUseCaseImpl implements IsAnAdminUserUseCase {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public IsAnAdminUserUseCaseImpl(GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public boolean isAdmin() {
        return getAuthenticatedUserUseCase.getAuthenticatedUser()
                .filter(user -> user.getRole().contains(UserRole.ADMIN))
                .isPresent();
    }
}
