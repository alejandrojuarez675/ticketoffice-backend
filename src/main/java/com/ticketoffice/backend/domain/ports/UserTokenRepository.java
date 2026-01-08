package com.ticketoffice.backend.domain.ports;

import com.ticketoffice.backend.domain.models.UserToken;
import java.util.Optional;

public interface UserTokenRepository {
    Optional<UserToken> save(UserToken model);
    Optional<UserToken> findByHashToken(String hashToken);
}
