package com.ticketoffice.backend.application.usecases.users;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.enums.UserRole;
import com.ticketoffice.backend.domain.usecases.users.GetAuthenticatedUserUseCase;
import com.ticketoffice.backend.domain.usecases.users.IsAnAdminUserUseCase;
import io.javalin.http.Context;

public class IsAnAdminUserUseCaseImpl implements IsAnAdminUserUseCase {

    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Inject
    public IsAnAdminUserUseCaseImpl(GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    public Boolean apply(Context ctx) {
        return getAuthenticatedUserUseCase.apply(ctx)
                .filter(user -> user.getRole().contains(UserRole.ADMIN))
                .isPresent();
    }
}
