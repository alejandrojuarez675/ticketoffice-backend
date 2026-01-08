package com.ticketoffice.backend.application.usecases.password;

import com.ticketoffice.backend.domain.enums.MailTemplates;
import com.ticketoffice.backend.domain.models.MailMessage;
import com.ticketoffice.backend.domain.models.UserToken;
import com.ticketoffice.backend.domain.models.User;
import com.ticketoffice.backend.domain.ports.MailSenderPort;
import com.ticketoffice.backend.domain.ports.UserTokenRepository;
import com.ticketoffice.backend.domain.usecases.password.ResetPasswordWithTokenUseCase;
import com.ticketoffice.backend.domain.usecases.password.UpdatePasswordUseCase;
import jakarta.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class ResetPasswordWithTokenUseCaseImpl implements ResetPasswordWithTokenUseCase {

    private final UserTokenRepository userTokenRepository;
    private final UpdatePasswordUseCase updatePasswordUseCase;
    private final MailSenderPort mailSenderPort;

    @Inject
    public ResetPasswordWithTokenUseCaseImpl(
            UserTokenRepository userTokenRepository,
            UpdatePasswordUseCase updatePasswordUseCase,
            MailSenderPort mailSenderPort
    ) {
        this.userTokenRepository = userTokenRepository;
        this.updatePasswordUseCase = updatePasswordUseCase;
        this.mailSenderPort = mailSenderPort;
    }

    @Override
    public Boolean apply(String token, String newPassword) {
        return userTokenRepository.findByHashToken(token)
                .filter(UserToken::isValidToken)
                .flatMap(x -> {
                    Optional<User> user = updatePasswordUseCase.apply(x.username(), newPassword);
                    markPasswordTokenAsUsed(x);
                    return user;
                })
                .map(this::sendNotificationToUser)
                .isPresent();
    }

    private void markPasswordTokenAsUsed(UserToken x) {
        UserToken passUserToken = new UserToken(x.id(), x.username(), x.email(), x.tokenHash(), x.expiresAt(), true);
        userTokenRepository.save(passUserToken);
    }

    private User sendNotificationToUser(User user) {
        MailMessage mail = new MailMessage(
                MailTemplates.PASSWORD_UPDATED,
                user.getEmail(),
                Map.of(
                        "username", user.getUsername()
                )
        );
        mailSenderPort.sendEmail(mail);
        return user;
    }
}
