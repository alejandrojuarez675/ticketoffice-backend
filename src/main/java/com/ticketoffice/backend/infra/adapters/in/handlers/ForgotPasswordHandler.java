package com.ticketoffice.backend.infra.adapters.in.handlers;

import com.ticketoffice.backend.domain.ports.UserRepository;
import com.ticketoffice.backend.domain.usecases.password.ForgotPasswordUseCase;
import com.ticketoffice.backend.domain.usecases.password.ResetPasswordWithTokenUseCase;
import com.ticketoffice.backend.infra.adapters.in.dto.request.ForgotPasswordRequest;
import com.ticketoffice.backend.infra.adapters.in.dto.request.ResetPasswordWithTokenRequest;
import jakarta.inject.Inject;

public class ForgotPasswordHandler {

    private final UserRepository userRepository;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordWithTokenUseCase resetPasswordWithTokenUseCase;

    @Inject
    public ForgotPasswordHandler(
            UserRepository userRepository,
            ForgotPasswordUseCase forgotPasswordUseCase,
            ResetPasswordWithTokenUseCase resetPasswordWithTokenUseCase
    ) {
        this.userRepository = userRepository;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordWithTokenUseCase = resetPasswordWithTokenUseCase;
    }


    public void forgotPassword(ForgotPasswordRequest body) {
        userRepository.findByEmail(body.email())
                .ifPresent(forgotPasswordUseCase);
    }

    public boolean resetPassword(ResetPasswordWithTokenRequest body) {
        return resetPasswordWithTokenUseCase.apply(body.token(), body.newPassword());
    }
}
