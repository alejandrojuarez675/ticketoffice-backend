package com.ticketoffice.backend.infra.adapters.out.db.repository.passwordresettoken;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.PasswordResetToken;
import com.ticketoffice.backend.domain.ports.PasswordResetTokenRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.PasswordResetTokenDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.PasswordResetTokenDynamoDBMapper;

import java.util.Optional;

public class PasswordResetTokenDynamoRepository implements PasswordResetTokenRepository {

    private final PasswordResetTokenDynamoDao passwordResetTokenDynamoDao;

    @Inject
    public PasswordResetTokenDynamoRepository(PasswordResetTokenDynamoDao passwordResetTokenDynamoDao) {
        this.passwordResetTokenDynamoDao = passwordResetTokenDynamoDao;
    }

    @Override
    public Optional<PasswordResetToken> save(PasswordResetToken token) {
        passwordResetTokenDynamoDao.save(PasswordResetTokenDynamoDBMapper.toMap(token));
        return Optional.of(token);
    }

    @Override
    public Optional<PasswordResetToken> findByHashToken(String hashToken) {
        return passwordResetTokenDynamoDao.findByHashToken(hashToken)
            .map(PasswordResetTokenDynamoDBMapper::fromMap);
    }
}
