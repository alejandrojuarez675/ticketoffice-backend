package com.ticketoffice.backend.infra.adapters.out.db.repository.passwordresettoken;

import com.google.inject.Inject;
import com.ticketoffice.backend.domain.models.UserToken;
import com.ticketoffice.backend.domain.ports.UserTokenRepository;
import com.ticketoffice.backend.infra.adapters.out.db.dao.UserTokenDynamoDao;
import com.ticketoffice.backend.infra.adapters.out.db.mapper.UserTokenDynamoDBMapper;

import java.util.Optional;

public class UserTokenDynamoRepository implements UserTokenRepository {

    private final UserTokenDynamoDao userTokenDynamoDao;

    @Inject
    public UserTokenDynamoRepository(UserTokenDynamoDao userTokenDynamoDao) {
        this.userTokenDynamoDao = userTokenDynamoDao;
    }

    @Override
    public Optional<UserToken> save(UserToken userToken) {
        userTokenDynamoDao.save(UserTokenDynamoDBMapper.toMap(userToken));
        return Optional.of(userToken);
    }

    @Override
    public Optional<UserToken> findByHashToken(String hashToken) {
        return userTokenDynamoDao.findByHashToken(hashToken)
            .map(UserTokenDynamoDBMapper::fromMap);
    }
}
